output "ASA_URI" {
#  value = "https://${jsondecode(azapi_resource.asa-app.output).properties.fqdn}"
  value = "https://${azapi_resource.asa.name}-${azapi_resource.asa-app.name}.azuremicroservices.io"
}

output "ASA_APP_IDENTITY_PRINCIPAL_ID" {
  value     = length(azapi_resource.asa-app.identity) == 0 ? "" : azapi_resource.asa-app.identity.0.principal_id
  sensitive = true
}

output "ASA_APP_DEPLOYMENT_ID" {
  value     = azapi_resource.deployment.id
  sensitive = true
}