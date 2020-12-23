##환경관련
>lang : java 8   
>framework : springboot   
>dbms : mysql   
>orm : jpa(hibernate)   


##end point
>뿌리기 : [post]127.0.0.1:8080/distribution   
>받기 : [put]127.0.0.1:8080/distribution   
>확인 : [get]127.0.0.1:8080/distribution   


##뿌리기 분배 관련
> 뿌리는 금액과 인원에 대한 비교작업(금액>인원)후 전체 인원을 뺀 나머지를 랜덤하게 계산해서 분배
>  
> ex) 금액 10000 인원 3 >> 각 인원에 1씩 분배했다고 가정후    
> 1-9997까지 랜덤한 수(2000으로 가정) 로 분배,    
> 그외 분배후 남은 금액 1-7997까지 랜덤하게 분배 후(3000으로 가정)    
>  남은 값을 n번째(3번째) 분배로 처리   
> > 2000+1, 3000+1, 4997+1 로 분배

##동시성 관련
> version 추가로 optimistic lock 처리,
> pessimistic lock으로 지연을 유도하는 방향보다 동시성 발생시 에러를 발생시키는게 낫다고 판단했습니다.