## What I learn
- 数组是引用数据类型，如果向函数中传入一个数组，在函数中对数组进行修改，那么原数组也会发生改变。 
- Is it OK to compare two floating-point numbers a and b for exactly equality? 
In general, it is hazardous to compare a and b for equality if either is susceptible to floating-point roundoff error. However, in this case, you are computing b/a, where a and b are integers between -32,767 and 32,767. In Java (and the IEEE 754 floating-point standard), the result of a floating-point operation (such as division) is the nearest representable value. Thus, for example, it is guaranteed that (9.0/7.0 == 45.0/35.0). In other words, it's sometimes OK to compare floating-point numbers for exact equality (but only when you know exactly what you are doing!) 
- Don't expose a private array to the client directly.
- It is a bad style to write code that depends on the particular format of the output.
- It is important to consider every situation clearly.
- How to deal with the time limit problem? try to decrease the most expensive operation.
