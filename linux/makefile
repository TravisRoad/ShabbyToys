list : list.c
	@gcc list.c -o list
	@./list
	
.PHONY:clean
clean:
	@rm list

.PHONY:test
test:
	./list -l 100 -h 5000 /bin /etc | head -n 30
	./list -a -r -l 50000 -m 2 | head
	./list -- -l | head
	./list * | head

.PHONY:deploy
deploy:
	sudo cp ./list /usr/bin