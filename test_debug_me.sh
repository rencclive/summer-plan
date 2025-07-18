#!/bin/bash

echo "🔍 调试 /api/auth/me 接口"
echo "=========================="

# 1. 先测试登录
echo "1. 测试登录获取token..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }')

echo "登录响应: $LOGIN_RESPONSE"

# 提取token
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "❌ 登录失败，无法获取token"
    exit 1
fi

echo "✅ 获取到token: ${TOKEN:0:20}..."

# 2. 测试 /api/auth/me 接口
echo ""
echo "2. 测试 /api/auth/me 接口..."
ME_RESPONSE=$(curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -w "\nHTTP状态码: %{http_code}")

echo "响应: $ME_RESPONSE"

# 3. 测试不带token的情况
echo ""
echo "3. 测试不带token访问 /api/auth/me..."
NO_TOKEN_RESPONSE=$(curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Content-Type: application/json" \
  -w "\nHTTP状态码: %{http_code}")

echo "响应: $NO_TOKEN_RESPONSE"

echo ""
echo "调试完成" 