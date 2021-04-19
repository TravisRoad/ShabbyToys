list : list.c
	@gcc list.c -o list
	@./list
	
.PHONY:clean
clean:
	@rm list

.PHONY:test
test:
	./list -l 100 -h 5000 /bin /etc
	./list -a -r -l 50000 -m 2
	./list -- -l
	./list *