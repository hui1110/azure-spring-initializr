locals {
  tags                         = { azd-env-name : var.environment_name, spring-cloud-azure : true }
  sha                          = base64encode(sha256("${var.environment_name}${var.location}${data.azurerm_client_config.current.subscription_id}"))
  resource_token               = substr(replace(lower(local.sha), "[^A-Za-z0-9_]", ""), 0, 13)
  psql_custom_username         = "CUSTOM_ROLE"
}
# ------------------------------------------------------------------------------------------------------
# Deploy resource Group
# ------------------------------------------------------------------------------------------------------
resource "azurecaf_name" "rg_name" {
  name          = var.environment_name
  resource_type = "azurerm_resource_group"
  random_length = 0
  clean_input   = true
}

resource "azurerm_resource_group" "rg" {
  name     = azurecaf_name.rg_name.result
  location = var.location

  tags = local.tags
}

# ------------------------------------------------------------------------------------------------------
# Deploy application insights
# ------------------------------------------------------------------------------------------------------
module "applicationinsights" {
  source           = "./modules/applicationinsights"
  location         = var.location
  rg_name          = azurerm_resource_group.rg.name
  environment_name = var.environment_name
  workspace_id     = module.loganalytics.LOGANALYTICS_WORKSPACE_ID
  tags             = azurerm_resource_group.rg.tags
  resource_token   = local.resource_token
}

# ------------------------------------------------------------------------------------------------------
# Deploy log analytics
# ------------------------------------------------------------------------------------------------------
module "loganalytics" {
  source         = "./modules/loganalytics"
  location       = var.location
  rg_name        = azurerm_resource_group.rg.name
  tags           = azurerm_resource_group.rg.tags
  resource_token = local.resource_token
}

# ------------------------------------------------------------------------------------------------------
# Deploy Azure PostgreSQL
# ------------------------------------------------------------------------------------------------------
module "postgresql" {
  source         = "./modules/postgresql"
  location       = var.location
  rg_name        = azurerm_resource_group.rg.name
  tags           = azurerm_resource_group.rg.tags
  resource_token = local.resource_token
}

# ------------------------------------------------------------------------------------------------------
# Deploy Azure Container Registry
# ------------------------------------------------------------------------------------------------------
module "acr" {
  source         = "./modules/containerregistry"
  location       = var.location
  rg_name        = azurerm_resource_group.rg.name
  resource_token = local.resource_token

  tags               = local.tags
}


# ------------------------------------------------------------------------------------------------------
# Deploy Azure Spring Apps api
# ------------------------------------------------------------------------------------------------------
module "asa_api" {
  name           = "asa-${local.resource_token}"
  app_name       = "asa-app-${local.resource_token}"
  source         = "./modules/springapps"
  location       = var.location
  rg_name        = azurerm_resource_group.rg.name

  tags               = merge(local.tags, { azd-service-name : "app" })

  cpu = "2"
  memory = "4Gi"

  env = {
    "APPLICATIONINSIGHTS_CONNECTION_STRING": module.applicationinsights.APPLICATIONINSIGHTS_CONNECTION_STRING,
    "AZURE_POSTGRESQL_URL": "jdbc:postgresql://${module.postgresql.AZURE_POSTGRESQL_FQDN}:5432/${module.postgresql.AZURE_POSTGRESQL_DATABASE_NAME}?sslmode=require",
    "AZURE_POSTGRESQL_USERNAME": local.psql_custom_username
  }

  identity = [{
    type = "SystemAssigned"
  }]
}


# ------------------------------------------------------------------------------------------------------
# Enable Passwordless for PostgreSQL
# ------------------------------------------------------------------------------------------------------
module "enable_passwordless" {
  source         = "./modules/passwordless"

  pg_custom_role_name_with_aad_identity = local.psql_custom_username
  pg_aad_admin_user = module.postgresql.AZURE_POSTGRESQL_ADMIN_USERNAME
  pg_database_name = module.postgresql.AZURE_POSTGRESQL_DATABASE_NAME
  pg_server_fqdn = module.postgresql.AZURE_POSTGRESQL_FQDN
  app_identity_principal_id = module.asa_api.ASA_APP_IDENTITY_PRINCIPAL_ID
}