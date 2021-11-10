const prompt = require('prompt-sync')();
const fs = require('fs');
const StringBuilder = require('string-builder');
const nboard = require('n-board');

// Main function that initiates nQueens problem algorithm
function nQueens(n) {
  cnfList = [];
  // Create the chess board
  board = createBoard(n);

  // Checks rows, columns, and diagonals for valid positions for n queens
  rows(n, cnfList, board);
  columns(n, cnfList, board);
  diagonals(n, cnfList, board);
  // Generates a cnf file with the possible positions for n queens
  generateCnfFile(n, cnfList)
}

// Generates a chess board with each position numbered for DIMACS format
function createBoard(n) {
  const gameBoard = nboard(n);
  let counter = 1;

  for (let i = 0; i < n; i++) {
    for (let j = 0; j < n; j++) {
      gameBoard[i][j] = counter;
      counter++;
    }
  }
  return gameBoard;
}

// Gets literals for queens and clause list based on rows
function rows(n, cnfList, gameBoard) {
  for(let i = 0; i < n; i++) {
    literals = [];
    for(let j = 0; j < n; j++) {
      literals.push(gameBoard[i][j]);
    }
    cnfList.push(literals);

    for(let i = 0; i < literals.length - 1; i++) {
      for(let j = i + 1; j < literals.length; j++) {
        clauseList = [];
        clauseList.push(-literals[i]);
        clauseList.push(-literals[j]);
        cnfList.push(clauseList);
      }
    }
  }
}

// Gets literals for queens and clause list based on columns
function columns(n, cnfList, gameBoard) {
  for(let i = 0; i < n; i++) {
    literals = [];
    for(let j = 0; j < n; j++) {
      literals.push(gameBoard[j][i]);
    }
    cnfList.push(literals);

    for(let i = 0; i < literals.length - 1; i++) {
      for(let j = i + 1; j < literals.length; j++) {
        clauses = [];
        clauses.push(-literals[i]);
        clauses.push(-literals[j]);
        cnfList.push(clauses);
      }
    }
  }
}

// Gets literals for queens and clause list based on all possible diagonals
function diagonals(n, cnfList, gameBoard) {
  for(let i = 0; i < n - 1; i++) {
    literals = [];
    for(let j = 0; j < n - i; j++) {
      literals.push(gameBoard[j][j + i]);
    }
    diagonalLines(literals, cnfList);
  }

  for(let i = 1; i < n - 1; i++) {
    literals = [];
    for(let j = 0; j < n - i; j++) {
      literals.push(gameBoard[j + i][j]);
    }
    diagonalLines(literals, cnfList);
  }

  for(let i = 0; i < n -1; i++) {
    literals = [];
    for(let j = 0; j < n - i; j++) {
      literals.push(gameBoard[j][n - 1 - (j + i)]);
    }
    diagonalLines(literals, cnfList);
  }

  for(let i = 1; i < n -1; i++) {
    literals = [];
    for(let j = 0; j < n - i; j++) {
      literals.push(gameBoard[j + i][n - 1 - j]);
    }
    diagonalLines(literals, cnfList);
  }
}

function diagonalLines(literals, cnfList) {
  for(let i = 0; i < literals.length - 1; i++) {
    for(let j = i + 1; j < literals.length; j++) {
      clauses = [];
      clauses.push(-literals[i]);
      clauses.push(-literals[j]);
      cnfList.push(clauses);
    }
  }
}

// Generated cnf file for SAT solver
function generateCnfFile(n, cnfList) {
  filePath = n + 'queens' + '.cnf';
  const sb = new StringBuilder();

  sb.append('c CNF File for n queens problem for SAT Solver\n');
  sb.append("c\n");
  sb.append("p cnf " + n * n + " " + cnfList.length + "\n");

  for(let i = 0; i < cnfList.length; i++) {
      sb.append(cnfList[i] + ' 0\n');
  }

  const data = sb.toString().replace(/,/g, ' ');
  fs.writeFile(filePath, data, function (err) {
    if (err) {
      return console.log('Write to file error: ', err);
    }
  });
}

// Prompt user for input n
const n = prompt('Enter value for n: ');

// Check if valid input
if (n < 1 || n > 10 ) {
  console.log('n must be greater than 0 and less than 5');
  return;
} else {
  nQueens(parseInt(n));
}
