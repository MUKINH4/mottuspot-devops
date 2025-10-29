#!/bin/bash

ACR_NAME="mottuspot"
RG_NAME="rg-mottu-spot"
IMAGE_NAME="mottu-spot"
TAG="latest"

# Criar Resource Group
echo "Criando Resource Group..."
az group create --name $RG_NAME --location eastus

# Criar Azure Container Registry
echo "Criando Azure Container Registry..."
az acr create --resource-group $RG_NAME --name $ACR_NAME --sku Basic --admin-enabled true

# Fazer login no ACR
echo "Fazendo login no ACR..."
az acr login --name $ACR_NAME

# Construir a imagem Docker
echo "Construindo a imagem Docker..."
docker build -t $IMAGE_NAME:$TAG .

# Tagear a imagem para o ACR
echo "Tageando a imagem para o ACR..."
docker tag $IMAGE_NAME:$TAG $ACR_NAME.azurecr.io/$IMAGE_NAME:$TAG

# Enviar a imagem para o ACR
echo "Enviando a imagem para o ACR..."
docker push $ACR_NAME.azurecr.io/$IMAGE_NAME:$TAG

echo "✅ Build e Push concluídos com sucesso!"
