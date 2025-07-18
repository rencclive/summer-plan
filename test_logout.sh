#!/bin/bash

echo "测试用户登出API..."

# 测试用户登出（不需要认证）
echo "发送登出请求..."
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Content-Type: application/json" \
  -w "\nHTTP状态码: %{http_code}\n"

echo ""
echo "测试完成" 