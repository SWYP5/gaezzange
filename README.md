# swyp5
swyp 5team gaezzange

## Get Started (LOCAL)

```
// 환경변수 설정 (optional)
 export MYSQL_ROOT_PASSWORD={root user password} // default: password
 export MYSQL_USER={user} // default: local
 export MYSQL_PASSWORD={user password} // default: password
 export SPRING_PROFILE_ACTIVE=local // default: local

// init.sh권한설정 필요
 chmod +x initdb/init.sh

// Docker 실행
 docker-compose up -d
 
Application 실행
```

## API Spec (Swagger)
```
http://localhost:8080/swagger-ui/index.html#/
```
