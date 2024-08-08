	.data
a:
	2
	26
	29
	20
	10
	158
	50
	60
	89
n:
	9
	.text
main:
	add %x0, %x0, %x3
	addi %x3, 1, %x4
	load %x0, $n, %x5
	subi %x5, 1, %x10
check:
	div %x4, %x5, %x6
	beq %x3, %x10, terminate
	beq %x31, %x0, index
	bgt %x31, %x0, loop
loop:
	load %x3, $a, %x7
	load %x4, $a, %x8
	blt %x7, %x8, interchange
	addi %x4, 1, %x4
	jmp check
index:
	addi %x3, 1, %x3
	addi %x3, 1, %x4
	jmp check
interchange:
	store %x8, $a, %x3
	store %x7, $a, %x4
	addi %x4, 1, %x4
	jmp check
terminate:
	end