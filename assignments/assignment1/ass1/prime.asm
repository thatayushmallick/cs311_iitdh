	.data
a:
	13
	.text
main:
	load %x0, $a, %x3
	subi %x3, 1, %x4
	addi %x0, 2, %x5
	addi %x0, 1, %x7
	beq %x3, %x5, istwo
loop:
	beq %x4, %x7, isprime
	div %x3, %x4, %x6
	beq %x31, %x0, notprime
	subi %x4, 1, %x4
	jmp loop
isprime:
	addi %x0, 1, %x10
	end
istwo:
	addi %x0, 1, %x10
	end
notprime:
	subi %x0, 1, %x10
	end