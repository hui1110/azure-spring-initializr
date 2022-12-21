locals {
  psql_custom_username         = "CUSTOM_ROLE"
}

resource "azapi_update_resource" "asa_deployment" {
  type        = "Microsoft.AppPlatform/Spring/apps/deployments@2022-11-01-preview"
  resource_id = var.asa_app_deployment_id

  body = jsonencode({
    properties = {
      active = true
      deploymentSettings = {
        environmentVariables = {
          "APPLICATIONINSIGHTS_CONNECTION_STRING": var.application_insights_connection_string,
          "AZURE_POSTGRESQL_URL": var.psql_url,
          "AZURE_POSTGRESQL_USERNAME": local.psql_custom_username
        }
        resourceRequests = {
          cpu = "2"
          memory = "4Gi"
        }
      }
      source = {
        type = "Container"
        customContainer = {
          containerImage = var.image_name
          server = "${var.container_registry_name}.azurecr.io"
          languageFramework = "SpringBoot"
          imageRegistryCredential = {
            username = var.container_registry_name
            password = var.container_registry_pwd
          }
        }
      }
    }
  })

}
