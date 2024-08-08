	.data
n:
	10
	.text
main:
	add %x0, %x0, %x3
	addi %x0, 1, %x4
	addi %x0, 65535, %x7
	store %x0, 0, %x7
	subi %x7, 1, $x7
	store %x4, 0, %x7
	subi %x7, 1, %x7
	addi %x0, 1, %x8
	load %x0, $n, %x9
loop:
	beq %x8, %x9, terminate
	add %x3, %x4, %x5
	addi %x5, 0, %x6
	store %x6, 0, %x7
	add %x0, %x4, %x3
	add %x0, %x6, %x4
	subi %x7, 1, %x7
	addi %x8, 1, %x8
	jmp loop
terminate:
	end