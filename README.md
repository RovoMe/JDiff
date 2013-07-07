JDIFF
=====

JDiff is a framework for comparing Java objects implementing the Comparable interface and is based on Eugene W. Myers paper 'An O(ND) Difference Algorithm and its Variations' published in 1986. 

The Java implementation presented is a port of the C# example published by Nicholas Butler (http://simplygenius.net/Article/DiffTutorial1, http://www.codeproject.com/Articles/42279/Investigating-Myers-diff-algorithm-Part-1-of-2) but does not provide a graphical user interface for visually presenting the algorithm.

The algorithm compares two sequences of objects with each other and builds a path where a equal object represents a diagonal move, a deletion of an object from the first object sequence a move to the right and an insertion from the second object sequence a move downwards. A segment between changes in the input sequences is called a snake.

The code was modified from the C# example to combine snakes that have the same action attached (Insertion, Deletion) without any other action being in-between to reduce the number of snakes returned. Furthermore, plenty of JavaDoc and comments have been added to describe the algorithm as far as I managed to understand the algorithm properly.

DiffTest in the Maven test tree visualizes the usage of the algorithm and further presents a way to print the differences of two HTML documents to the standard output.
