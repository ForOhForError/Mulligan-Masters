# Mulligan-Masters
Tool to help calculate the odds of different opening hands

USAGE:

Run the provided jar file (or the MulliganMasters class file if running from source) to start the GUI.

In the deck textbox, enter the decklist in the following format, one card per line:
```
<Number of copies>{<Attibute 1>;<Attibute 2>;...;<Attibute N>)<Card Name>
```
Enter test cases into the top textbox. Begin each test case with a line beginning with `#`. The following parameters can be used on this line as well:

**name**: The name of the test case. Defaults to `Case <Test Case Number>`

**runs**: The number of times to run this test case. A higher number will give higher accuracy. Defaults to 1000.

**hand**: The starting hand size to use. Defaults to 7.

**mullto**: The hand size to mulligan down to. The program will mulligan until it hits an acceptable hand, or until this hand size is reached. Defaults to 7.

Follow this line by a description of acceptable hands for this test case, formatted the following way:
`{<Attribute 1>:<Count 1>[+/-/=];<Attribute 2>:<Count 2>[+/-/=];...;<Attribute N>:<Count N>[+/-/=]}`

The program will count occurences of each attribute in each hand it draws, and compare them to the count of that attribute according to the mode specified. `+` will match the given number or higher, `-` will match the given number or lower, and `=` will match the given number exactly. Specifying no mode is identical to specifying `+`.

The program also handles boolean logic with test cases. Within a case, you can use the `AND`, `OR`, and `NOT` operators (as well as parentheses) between cases enclosed in curly braces. Take for example this case from the included tron decklist:

```
#name:Active Tron,runs:10000,hand:7,mullto:6
{tower:1+;plant:1+;mine:1+;payload:1+;tron:1+;find:2+}
OR
{tower:1+;plant:1+;mine:1+;payload:1+;tron:2+;find:1+}
```

Click run to get the results of all test cases. Can save and load decklists in its own format.
