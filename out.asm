.data
i: .space 4
c: .space 4
sb: .space 24
iArray: .space 40
cArray: .space 12
p: .space 4
######end of declaration######
.text
jal main
######declaration of functions######

main:
addi $sp, $sp, 0
move  $t9, $fp
move  $fp, $sp
add $sp, $sp, -12
###visit block###
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
###block vd↑###block stmt↓###
li $s1, 'x'
sb $s1, -20($fp), # visitAssign Tag 4
li $s1, 'h'
li $t7, 0
li $t2, 4
mult $t2, $t7
mflo $t2
la $s2,cArray
add $t2, $t2, $s2
sw $s1, ($t2), # visitAssign Tag 6
li $s1, 'i'
li $t7, 1
li $t2, 4
mult $t2, $t7
mflo $t2
la $t4,cArray
add $t2, $t2, $t4
sw $s1, ($t2), # visitAssign Tag 6
li $s1, '!'
li $t7, 2
li $t2, 4
mult $t2, $t7
mflo $t2
la $s7,cArray
add $t2, $t2, $s7
sw $s1, ($t2), # visitAssign Tag 6
li $s1, '\0'
li $t7, 3
li $t2, 4
mult $t2, $t7
mflo $t2
la $t5,cArray
add $t2, $t2, $t5
sw $s1, ($t2), # visitAssign Tag 6
li $s1, 6
sw $s1, -12($fp), # visitAssign Tag 3
li $s1, 0
sw $s1, -16($fp), # visitAssign Tag 3
whileStart1:
lw $s1, -16($fp)
lw $t2, -12($fp)
slt $t7, $s1, $t2
beq $t7, $zero, whileEnd1
###visit block###
###block vd↑###block stmt↓###
# print_i
lw $s1, -16($fp)
li $v0, 1
add $a0, $zero, $s1
syscall
# print_i

lw $s1, -16($fp)
li $t2, 1
add $t5, $s1, $t2
sw $t5, -16($fp), # visitAssign Tag 3
###visit block end###
b whileStart1
whileEnd1:
lb $s1, -20($fp)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

la $t2,cArray
# print_s
li $v0, 4
la $a0, ($t2)
syscall
# print_s

# print_i
lw $t2, -12($fp)
li $t7, 1
add $t5, $t2, $t7
li $v0, 1
add $a0, $zero, $t5
syscall
# print_i

.data
s1: .asciiz "Print!"
.text
la $t2, s1
# print_s
li $v0, 4
la $a0, ($t2)
syscall
# print_s

li $t2, 0
li $t7, 1
sub $t5, $t2, $t7
add   $v0, $zero, $t5
li $v0, 17
syscall
jr $ra
li $t2, 0
add   $v0, $zero, $t2
li $v0, 17
syscall
jr $ra
###visit block end###
