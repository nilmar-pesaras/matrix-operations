import java.util.Scanner;
public class Matrix {
    public static void main (String [] args){

        Scanner user_input = new Scanner (System.in);
        mt_operation();

        boolean repeat_loop = true;
        try{
            while(repeat_loop) {
                System.out.print("Would you like to do it all again? (Y/N): ");
                char answer1 = user_input.next().charAt(0);
                if (answer1 == 'Y' || answer1 == 'y') {
                    mt_operation();
                }
                else if (answer1 == 'N' || answer1 == 'n') {
                    repeat_loop = false;
                    System.out.println("The program is now terminated.");
                }
                else {
                    System.out.println("Input invalid, input Y/N only. Please try again!");
                }
            }
        } catch (Exception e){
            System.out.println("Input invalid. Please try again!");
        } user_input.close();
    }

    //Main flow of the program
    public static void mt_operation() {
        try{

            Scanner user_input = new Scanner(System.in);
            //System.out.print("Enter the number of matrices to be computed: ");

            //int matrixNum = user_input.nextInt();
            int matrixNum = 2;
            int[][][] matrices = new int[matrixNum][][];

            for (int i = 0; i < matrixNum; i++) {
                try{
                    //input matrix dimensions
                    System.out.println();
                    System.out.println("Enter Matrix " + (i + 1));
                    System.out.print("Number of rows: ");
                    int rows_num = user_input.nextInt();

                    System.out.print("Number of columns: ");
                    int cols_num = user_input.nextInt();

                    if (rows_num > 10 || cols_num > 10) {
                        System.out.println("Matrix Dimension limit is at 10x10. Please try again!");
                    }
                    else if (rows_num <= 0 || cols_num <= 0) {
                        System.out.println("Matrix Dimension must be greater than zero. Please try again!");
                    }

                    int[][] matrix = new int[rows_num][cols_num]; //This is where the elements of the matrix will be stored.

                    for (int j = 0; j < rows_num; j++) {
                        for (int k = 0; k < cols_num; k++) {
                            try {
                                System.out.print("Enter Row " + (j + 1) + ", Column " + (k + 1) + " value: ");
                                matrix[j][k] = user_input.nextInt();
                            } catch (Exception e){
                                System.out.println ("Invalid Input! Please enter a number: ");
                                user_input.next();
                                k--;
                            }
                        }
                    }
                    matrices[i] = matrix;
                    System.out.println();

                    // compare the number of columns of the previous matrix with the number of rows of the current matrix
                    if (i > 0 && matrices[i - 1][0].length != matrices[i].length) {
                        System.out.println("Entered matrices are not conformable for multiplication. Please try again!");
                        return;
                    }
                }catch (Exception e){ //This is the exception handling for the input of the matrix.
                    System.out.println ("Invalid Input! Please enter a number: ");
                    user_input.next();
                    i--;
                }
            }

            //In this portion, the program will print out the elements of the entered matrices.
            for (int i=0; i < matrixNum; i++) {
                char userLetter = 0;
                userLetter = 'A';
                System.out.println("The Elements of Matrix " + (char)(userLetter+i)+ " are:");
                outputMatrix(matrices [i]);
            }

            //In this portion of the code, the user is asked on the operations that will be used for the
            //matrices that they have entered.
            boolean operation_loop = true;
            while (operation_loop) {
                System.out.println("Choose an operation to be executed: ");
                System.out.println("1. Addition");
                System.out.println("2. Multiplication");
                System.out.println("3. Constant");
                System.out.println("4. Diagonalizing");
                System.out.print("Select the corresponding number of the operation to be used:");
                char operation = user_input.next().charAt(0);

                int[][] result;
                if (operation == '1') {
                    try{
                        int[][] sum = add(matrices);
                        System.out.println("The matrices' sum is: ");
                        System.out.print("A + B = ");
                        outputMatrix(sum);

                        boolean addAnotherMatrix = true;
                        int[][] newSum = sum;
                        while (addAnotherMatrix) {
                            int[][] newMatrix;
                            System.out.print("Would you like to add another matrix? (Y/N): ");
                            char response = user_input.next().charAt(0);
                            if (response == 'Y' || response == 'y') {
                                boolean conformable;
                                do {
                                    newMatrix = add_newMatrixAdd(newSum);
                                    conformable = checkConformabilityForAddition(newSum, newMatrix);
                                    if (!conformable) {
                                        System.out.println("Entered matrices are not conformable. Please try again!");
                                    }
                                } while (!conformable);
                                newSum = newMatrix;
                            } else if (response == 'N' || response == 'n') {
                                addAnotherMatrix = false;
                            } else {
                                System.out.println("Input invalid, input Y/N only. Please try again!");
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                } else if (operation == '2') {
                    try {
                        int[][] product = multiply(matrices);
                        System.out.println("The matrices' product is: ");
                        outputMatrix(product);
                        boolean multiplyAnotherMatrix = true;
                        while (multiplyAnotherMatrix) {
                            System.out.print("Would you like to multiply another matrix? (Y/N): ");
                            char response = user_input.next().charAt(0);
                            if (response == 'Y' || response == 'y') {
                                boolean conformable;
                                int[][] newMatrix;
                                do {
                                    newMatrix = add_newMatrixMulti(matrices, product);
                                    conformable = checkConformabilityForMultiplication(product, newMatrix);
                                    if (!conformable) {
                                        System.out.println("Entered matrices are not conformable. Please try again!");
                                    }
                                } while (!conformable);
                                product = newMatrix;
                            } else if (response == 'N' || response == 'n') {
                                multiplyAnotherMatrix = false;
                            } else {
                                System.out.println("Input invalid, input Y/N only. Please try again!");
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (operation == '3') {
                    try {
                        System.out.print("Enter the constant to be multiplied to the matrices: ");
                        int m_constant = user_input.nextInt();

                        for (int i = 0; i < matrixNum; i++) {
                            System.out.println("The Elements of Matrix " + (i + 1) + " are:");
                            outputMatrix(matrices[i]);

                            result = new int[matrices[i].length][matrices[i][0].length];
                            for (int j = 0; j < matrices[i].length; j++) {
                                for (int k = 0; k < matrices[i][0].length; k++) {
                                    result[j][k] = m_constant * matrices[i][j][k];
                                }
                            }
                            System.out.println("The product of the matrix and the constant is: ");
                            outputMatrix(result);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (operation == '4') {
                    try {
                        for (int i = 0; i < matrixNum; i++) {
                            result = new int[matrices[i].length][matrices[i][0].length];
                            for (int j = 0; j < matrices[i].length; j++) {
                                for (int k = 0; k < matrices[i][0].length; k++) {
                                    if (j == k) {
                                        result[j][k] = matrices[i][j][k];
                                    }else {
                                        result[j][k] = 0;
                                    }
                                }
                            }
                            System.out.println();
                            System.out.println("The diagonalized matrix is: ");
                            outputMatrix(result);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Please choose a valid number based on the choices");
                    continue;
                }

                // This will ask if the user wants to perform another operation using the same matrices that they input.
                System.out.print("Would you like to make use of the same matrices to perform another operation? (Y/N): ");
                char answer2 = user_input.next().charAt(0);
                if (answer2 == 'N' || answer2 == 'n') {
                    break;
                }
            }
        }catch (Exception e){
            System.out.println("Input invalid. Please try again!");
        }
    }

    //Method add_newMatrix() is responsible for creating and inputting a new matrix to be added to the existing sum in the addition operation.
    public static int[][] add_newMatrixAdd(int[][] newSum) {
        Scanner user_input = new Scanner(System.in);

        int rows_num, cols_num;
        boolean conformable;
        int[][] newMatrix;

        try{
            do {
                System.out.print("Enter Matrix Name: ");
                char name = user_input.next().charAt(0);
                System.out.print("Number of rows: ");
                rows_num = user_input.nextInt();
                System.out.print("Number of columns: ");
                cols_num = user_input.nextInt();

                if (rows_num > 10 || cols_num > 10) {
                    System.out.println("Matrix Dimension limit is at 10x10. Please try again!");
                    conformable = false;
                } else if (rows_num <= 0 || cols_num <= 0) {
                    System.out.println("Matrix Dimension must be greater than zero. Please try again!");
                    conformable = false;
                } else {
                    // Check conformability for addition manually.
                    if (newSum.length == rows_num && newSum[0].length == cols_num) {
                        newMatrix = new int[rows_num][cols_num];
                        System.out.println("Enter the elements of the new matrix: ");
                        for (int i = 0; i < rows_num; i++) {
                            for (int k = 0; k < cols_num; k++) {
                                try {
                                    System.out.print("Enter Row " + (i + 1) + ", Column " + (k + 1) + " value: ");
                                    newMatrix[i][k] = user_input.nextInt();
                                } catch (Exception e) {
                                    System.out.println("Invalid Input! Please enter a number: ");
                                    user_input.next();
                                    k--;
                                }
                            }
                        }

                        System.out.println("\nThe Sum of the Previous Matrices are:");
                        outputMatrix(newSum);
                        System.out.println("\nThe Elements of Matrix " + (name) + " are:");
                        outputMatrix(newMatrix);

                        // Update the sum with the result of adding the new matrix to the current sum.
                        newSum = add(newSum, newMatrix);

                        // Output the updated sum.
                        System.out.println("\nNew Sum Matrix:");
                        outputMatrix(newSum);

                        conformable = true;
                    } else {
                        System.out.println("The given matrices are not conformable for addition. Please try again!");
                        conformable = false;
                    }
                }
            } while (!conformable);
        } catch (Exception e) {
            System.out.println("Invalid Input! ");
        }
        return newSum;
    }
    public static int[][] add_newMatrixMulti(int[][][] matrices, int[][] product) {
        Scanner user_input = new Scanner(System.in);
        int rows_num, cols_num;
        boolean conformable = false;

        try{
            while (!conformable) {
                System.out.print("Enter Matrix Name: ");
                char name = user_input.next().charAt(0);
                System.out.print("Number of rows: ");
                rows_num = user_input.nextInt();
                System.out.print("Number of columns: ");
                cols_num = user_input.nextInt();

                if (rows_num > 10 || cols_num > 10) {
                    System.out.println("Matrix Dimension limit is at 10x10. Please try again!");
                } else if (rows_num <= 0 || cols_num <= 0) {
                    System.out.println("Matrix Dimension must be greater than zero. Please try again!");
                } else {

                    if (product.length == rows_num && product[0].length == cols_num) {
                        int[][] newMatrix = new int[rows_num][cols_num];
                        System.out.println("Enter the elements of the new matrix: ");
                        for (int i = 0; i < rows_num; i++) {
                            for (int k = 0; k < cols_num; k++) {
                                try {
                                    System.out.print("Enter Row " + (i + 1) + ", Column " + (k + 1) + " value: ");
                                    newMatrix[i][k] = user_input.nextInt();
                                } catch (Exception e){
                                    System.out.println ("Invalid Input! Please enter a number: ");
                                    user_input.next();
                                    k--;
                                }
                            }
                        }

                        System.out.println("\nThe Product of the Previous Matrices are: ");
                        outputMatrix(product);

                        System.out.println("\nThe Elements of Matrix " + (name) + " are:");
                        outputMatrix(newMatrix);

                        int[][] newProduct = multiply(product, newMatrix);
                        if (newProduct != null) {
                            product = newProduct;
                            conformable = true;
                        } else {
                            System.out.println("The given matrices are not conformable for multiplication. Please try again!");
                        }
                    } else {
                        System.out.println("The given matrices are not conformable for multiplication. Please try again!");
                    }
                }
            }
        } catch (Exception e){
            System.out.println ("Invalid Input!");
        }
        System.out.println("\nNew Product Matrix:");
        outputMatrix(product);
        return product;
    }

    // Method checkConformabilityForAddition() checks if two matrices can be added by comparing their dimensions.
    public static boolean checkConformabilityForAddition(int[][] matrix1, int[][] matrix2) {
        return matrix1.length == matrix2.length && matrix1[0].length == matrix2[0].length;
    }

    // Method checkConformabilityForMultiplication() checks if two matrices can be multiplied
    // by comparing the number of columns in the first matrix with the number of rows in the second matrix.
    public static boolean checkConformabilityForMultiplication(int[][] matrix1, int[][] matrix2) {
        return matrix1[0].length == matrix2.length;
    }

    // Method add() receives an array of matrices and returns their sum.
    // It first checks if the matrices are conformable, and then performs the addition.
    public static int [][] add(int [][][] mt_input){

        int rownum = mt_input[0].length;
        int colnum = mt_input[0][0].length;
        if(rownum != colnum){
            throw new IllegalArgumentException("Entered matrices are not conformable. Please try again!");
        }

        //A new array is initialized in order to store more matrices.
        //This loop corresponds to the operation in which will add the matrices involved.
        //In short, this loop is where the addition will take place.
        int [][] p = new int [rownum][colnum];
        for (int[][] inputMatrix : mt_input) {
            for (int i = 0; i < rownum; i++) {
                for (int j = 0; j < colnum; j++) {
                    p[i][j] = inputMatrix[i][j] + p[i][j];
                }
            }
        }
        return p;
    }

    // Method add() overloaded version that adds two matrices together and returns their sum.
    public static int[][] add(int[][] matrix1, int[][] matrix2) {
        //This code refers to the addition method of the matrices.
        int rownum = matrix1.length;
        int colnum = matrix1[0].length;

        if (rownum != matrix2.length || colnum != matrix2[0].length) {
            throw new IllegalArgumentException("Entered matrices are not conformable. Please try again!");
        }

        //A new array is initialized in order to store more matrices.
        //This loop corresponds to the opperation in which will add the matrices involved.
        //In short, this loop is where the addition will take place.
        int[][] sum = new int[rownum][colnum];
        for (int i = 0; i < rownum; i++) {
            for (int j = 0; j < colnum; j++) {
                sum[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return sum;
    }

    // Method multiply() receives an array of matrices and returns their product.
    // It first checks if the matrices are conformable, and then performs the multiplication.
    public static int[][] multiply(int[][][] mt_input) {
        // This code refers to the multiplication method of the matrices.
        int num_matrix = mt_input.length;
        for (int i = 1; i < num_matrix; i++) {
            if (mt_input[i - 1][0].length != mt_input[i].length) {
                throw new IllegalArgumentException("Entered matrices are not conformable. Please try again!");
            }
        }

        // This loop corresponds to the operation in which will multiply the matrices involved.
        // In short, this loop is where the multiplication will take place.
        int[][] q = mt_input[0];
        for (int i = 1; i < num_matrix; i++) {
            q = multiply(q, mt_input[i]);
        }
        return q;
    }
    // Method multiply() overloaded version that multiplies two matrices together and returns their product.
    public static int[][] multiply(int[][] matrix1, int[][] matrix2) {
        if (matrix1[0].length != matrix2.length) {
            throw new IllegalArgumentException("Entered matrices are not conformable. Please try again!");
        }

        int[][] product = new int[matrix1.length][matrix2[0].length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    product[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return product;
    }


    // Method outputMatrix() is responsible for displaying the elements of a given matrix.
    public static void outputMatrix(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        // Calculate the maximum number of digits in any element of the matrix
        int maxDigits = 1;
        for (int[] row : matrix) {
            for (int elem : row) {
                int numDigits = String.valueOf(elem).length();
                if (numDigits > maxDigits) {
                    maxDigits = numDigits;
                }
            }
        }

        // Calculate the width of each imaginary box cell
        int cellWidth = maxDigits + 2;

        // Print the top border of the imaginary box
        for (int i = 0; i < (numCols * cellWidth) + 1; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Print the matrix elements and the left and right borders of the imaginary box
        for (int i = 0; i < numRows; i++) {
            System.out.print("|");
            for (int j = 0; j < numCols; j++) {
                String elemStr = String.valueOf(matrix[i][j]);
                int numSpaces = cellWidth - elemStr.length();
                int numLeftSpaces = numSpaces / 2;
                int numRightSpaces = numSpaces - numLeftSpaces;
                for (int k = 0; k < numLeftSpaces; k++) {
                    System.out.print(" ");
                }
                System.out.print(elemStr);
                for (int k = 0; k < numRightSpaces; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println("|");
        }

        // Print the bottom border of the imaginary box
        for (int i = 0; i < (numCols * cellWidth) + 1; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
