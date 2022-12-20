terraform {
  required_providers {
    azurerm = {
      version = "~>3.33.0"
      source  = "hashicorp/azurerm"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = "~>1.2.15"
    }
    azapi = {
      source = "Azure/azapi"
      version = "~>1.1.0"
    }
  }
}

data "azurerm_subscription" "current" {}

# ------------------------------------------------------------------------------------------------------
# Deploy Azure Spring Apps
# ------------------------------------------------------------------------------------------------------
resource "azapi_resource" "asa" {
  type = "Microsoft.AppPlatform/Spring@2022-11-01-preview"
  name     = var.name
  location = var.location
  parent_id = "/subscriptions/${data.azurerm_subscription.current.subscription_id}/resourceGroups/${var.rg_name}"
  tags = var.tags
  body = jsonencode({
    sku = {
      name = "S0"
      tier = "Standard"
    }
  })
}

resource "azapi_resource" "asa-app" {
  type = "Microsoft.AppPlatform/Spring/apps@2022-11-01-preview"
  name = "default"
  location = var.location
  parent_id = azapi_resource.asa.id

  dynamic "identity" {
    for_each = { for k, v in var.identity : k => v if var.identity != [] }
    content {
      type = identity.value["type"]
    }
  }

  body = jsonencode({
    properties = {
      public = true
    }
  })

  response_export_values = ["*"]
}

resource "azapi_resource" "deployment" {
  type = "Microsoft.AppPlatform/Spring/apps/deployments@2022-11-01-preview"
  name = "default"
  parent_id = azapi_resource.asa-app.id
  body = jsonencode({
    properties = {
      active = true
      deploymentSettings = {
        environmentVariables = var.env
        resourceRequests = {
          cpu = var.cpu
          memory = var.memory
        }
      }
      source = {
        type = "Container"
        customContainer = {
#          server = "${var.container_registry_name}.azurecr.io"
#          containerImage = "springio/gs-spring-boot-docker:latest"
#          languageFramework = "SpringBoot"
#          imageRegistryCredential = {
#            username = var.container_registry_name
#            password = var.container_registry_pwd
#          }
          containerImage = "springio/gs-spring-boot-docker:latest"
          server = "docker.io"
          languageFramework = "SpringBoot"
        }
      }
    }
  })
}

