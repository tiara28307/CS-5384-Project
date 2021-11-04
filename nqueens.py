#!/usr/bin/env python2.7
# python script to generate SAT encoding of N-queens problem
#
# Jeremy Johnson and Mark Boady

import sys
#Helper Functions
#cnf formula for exactly one of the variables in list A to be true
def exactly_one(A):
	temp=""
	temp=temp+atleast_one(A)
	temp=temp+atmost_one(A)
	return temp
#cnf formula for atleast one of the variables in list A to be true
def atleast_one(A):
	temp=""
	for x in A:
		temp = temp +" " +str(x)
	temp=temp+" 0\n"
	return temp
#cnf formula for atmost one of the variables in list A to be true
def atmost_one(A):
	temp=""
	for x in A:
		for y in A[A.index(x):]:
			temp = temp +" -"+str(x)+" -"+str(y)+" 0\n"
	return temp
#function to map position (r,c) 0 <= r,c < N, in an NxN grid to the integer
# position when the grid is stored linearly by rows.
def varmap(r,c,N):
     return r*N+c+1

#Read Input
if len(sys.argv)>1:
	N=int(sys.argv[1])
else:
	N=3
#Check for Sane Input
if N<1:
	print("Error N<1")
	sys.exit(0)
#Start Solver
print("c SAT Expression for N="+str(N))
spots = N*N
print("c Board has "+str(spots)+" positions")

#Exactly 1 queen per row
temp=""
for row in range(0,N):
	A=[]
	for column in range(0,N):
		position = varmap(row,column,N)
		A.append(position)
	temp = temp+exactly_one(A)
#Exactly 1 queen per column
for column in range(0,N):
	A=[]
	for row in range(0,N):
		position = varmap(row,column,N)
		A.append(position)
	temp = temp+exactly_one(A)
#Atmost 1 queen per negative diagonal from left
for row in range(N-1,-1,-1):
	A=[]
	for x in range(0,N-row):
		A.append(varmap(row+x,x,N))
	temp=temp+atmost_one(A)
#Atmost 1 queen per negative diagonal from top
for column in range(1,N):
	A=[]
	for x in range(0,N-column):
		A.append(varmap(x,column+x,N))
	temp=temp+atmost_one(A)
#Atmost 1 queen per positive diagonal from right
for row in range(N-1,-1,-1):
	A=[]
	for x in range(0,N-row):
		A.append(varmap(row+x,N-1-x,N))
	temp=temp+atmost_one(A)
#Atmost 1 queen per positive diagonal from top
for column in range(N-2,-1,-1):
	A=[]
	for x in range(0,column+1):
		A.append(varmap(x,column-x,N))
	temp=temp+atmost_one(A)
print 'p cnf ' + str(N*N) + ' ' + str(temp.count('\n')) + '\n'
print(temp)
