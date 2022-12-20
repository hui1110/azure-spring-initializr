variable "location" {
  description = "The supported Azure location where the resource deployed"
  type        = string
}

variable "rg_name" {
  description = "The name of the resource group to deploy resources into"
  type        = string
}

variable "tags" {
  description = "A list of tags used for deployed services."
  type        = map(string)
}

variable "identity" {
  description = "A list of application identity"
  type        = list(any)
  default     = []
}

variable "name" {
  description = "Name of Container App"
  type        = string
}

variable "app_name" {
  description = "Name of Container App"
  type        = string
}

variable "env" {
  description = "Environment variables of Container App"
  type        = map(string)
  default = {}
}

variable "cpu" {
  description = "CPU of Container App"
  type        = string
  default = "1"
}

variable "memory" {
  description = "Memory of Container App"
  type        = string
  default = "2.0Gi"
}
