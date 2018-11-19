.data
i: .space 4
######end of declaration######
.text
jal main
######declaration of functions######

a:
# Save fp,sp,rt
sw $fp, 0($sp)
sw $sp, -4($sp)
# End of save fp,sp,rt
addi $sp, $sp, 0
move  $fp, $sp
###visit block###
###block vd↑###block stmt↓###
li $t9, 3
la $t8, i
sw $t9, ($t8), # visitAssign Tag 1
# restore fp,sp,rt
lw $fp, 0($sp)
lw $sp, -4($sp)
# restore of save fp,sp,rt
jr $ra
# restore fp,sp,rt
lw $fp, 0($sp)
lw $sp, -4($sp)
# restore of save fp,sp,rt
jr $ra
###visit block end###

foo:
# Save fp,sp,rt
sw $fp, 0($sp)
sw $sp, -4($sp)
# End of save fp,sp,rt
addi $sp, $sp, 0
move  $fp, $sp
###visit block###
###block vd↑###block stmt↓###
# print_i
la $t8,i
lw $t9,($t8)
li $v0, 1
add $a0, $zero, $t9
syscall
# print_i

# restore fp,sp,rt
lw $fp, 0($sp)
lw $sp, -4($sp)
# restore of save fp,sp,rt
jr $ra
###visit block end###

main:
addi $sp, $sp, 0
move  $fp, $sp
###visit block###
add $sp, $sp, -4
###block vd↑###block stmt↓###
# read_i
li $v0, 5
syscall
# read_i

add $t9 $v0, $zero
sw $t9, -12($fp), # visitAssign Tag 3
jal a
# print_i
lw $t9, -12($fp)
li $v0, 1
add $a0, $zero, $t9
syscall
# print_i

jal foo
li $v0, 10
syscall
jr $ra
###visit block end###
