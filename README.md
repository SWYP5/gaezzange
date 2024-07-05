# swyp5
swyp 5team gaezzange

## Get Started (LOCAL)

```
// docker 환경변수 설정 (optional)
 export MYSQL_ROOT_PASSWORD={root user password} // default: password
 export MYSQL_USER={user} // default: local
 export MYSQL_PASSWORD={user password} // default: password
 
// spring 환경변수 설정
 export SPRING_PROFILE_ACTIVE=local 
 or
 Intellij 환경 변수 설정
 

// init.sh권한설정 필요
 chmod +x initdb/init.sh

// Docker 실행
 docker-compose up -d
```
