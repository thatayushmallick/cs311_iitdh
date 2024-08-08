	.data
a:
	10 
    .text
main:
    load %x0, $a, %x3
    addi %x0, 2, %x4
    addi %x0, 1, %x5
    div %x3, %x4, %x6
    beq %x31, %x5, Odd
    beq %x31, %x0, Even
Odd:
    addi %x0, 1, %x10
    end
Even:
    subi %x0, 1, %x10
    end