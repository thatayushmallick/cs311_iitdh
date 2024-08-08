	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	add %x3, %x0, %x4
	addi %x0, 10, %x5
	add %x0, %x0, %x6
loop:
	beq %x3, %x0, terminate
	div %x3, %x5, %x3
	mul %x6, %x5, %x6
	add %x6, %x31, %x6
	jmp loop
terminate:
	beq %x6, %x4, success
	bne %x6, %x4, failure
success:
	addi %x0, 1, %x10
	end
failure:
	subi %x0, 1, %x10
	end