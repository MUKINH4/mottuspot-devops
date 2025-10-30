#!/bin/bash

ACR_NAME="mottuspot"
RG_NAME="rg-mottu-spot"
IMAGE_NAME="mottu-spot:latest"

# Criar Resource Group
echo "Criando Resource Group..."
az group create --name $RG_NAME --location eastus

# Criar Azure Container Registry
echo "Criando Azure Container Registry..."
az acr create --resource-group $RG_NAME --name $ACR_NAME --sku Basic --admin-enabled true

# Fazer login no ACR
echo "Fazendo login no ACR..."
az acr login --name $ACR_NAME

docker pull postgres:17-alpine
docker tag postgres:17-alpine $ACR_NAME.azurecr.io/postgres:17-alpine
docker push $ACR_NAME.azurecr.io/postgres:17-alpine


