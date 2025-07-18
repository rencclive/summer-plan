#!/bin/bash
# 用法: ./test_task_progress_post.sh <token> <taskId> <progress> [comment]

TOKEN=$1
TASK_ID=$2
PROGRESS=$3
COMMENT=${4:-"进度更新测试"}

if [ -z "$TOKEN" ] || [ -z "$TASK_ID" ] || [ -z "$PROGRESS" ]; then
  echo "用法: $0 <token> <taskId> <progress> [comment]"
  exit 1
fi

API_URL="http://localhost:8080/api/tasks/$TASK_ID/progress"

curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"progress":'$PROGRESS', "comment":"'$COMMENT'"}'

echo 