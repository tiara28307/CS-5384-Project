# CS-5384-Project
Encode the n-queens problem into the DIMACS CNF format and solve it by SAT solver.

## Install
Must have the following installed to run program:
- Node.js: https://nodejs.org/en/download/
- NPM
- Minisat: http://minisat.se/

## Run
1. Install npm dependencies: ``npm install``
2. Run ``node nqueens.js``
3. Will prompt you to enter value for n (for runtime we set a max value of 10 for n)
4. Program will produce a .cnf file named: <n_value_entered>queens.cnf (e.g., 4queens.cnf)
5. Run the SAT solver with cnf file as input: e.g., ``minisat 4queens.cnf``

Developers:
- Ti'Ara Carroll
- Claudia Gallosa
- Nabonita Mitra
