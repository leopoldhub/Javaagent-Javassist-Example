`java -javaagent:./agent/target/agent-1.0-SNAPSHOT-jar-with-dependencies.jar -jar ./application/target/application-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Rappel javassist

|special variable       | description                                           |
|-----------------------|-------------------------------------------------------|
| $0, $1, $2, ... 	    | this and actual parameters                            |
| $args 	            | An array of parameters. The type of $args is Object[].|
| $$ 	                | All actual parameters.                                |

```java
m.setBody("{" +
        "     System.out.println(\"args: \"+$1+\" et \"+$2);" +
        "     int c = 5;" +
        "     System.out.println(\"c=\"+c);" +
        "     return $1 + $2;" +
        "  }");
```