[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/mauriciotogneri/inquiry/blob/master/LICENSE.md)
[![Download](https://api.bintray.com/packages/mauriciotogneri/maven/inquiry/images/download.svg)](https://bintray.com/mauriciotogneri/maven/inquiry/_latestVersion)

# Inquiry
Object oriented queries for JDBC.

## Example

Given that we have a JDBC connection:
```java
Connection connection = // obtain connection
```

Select:
```java
SelectQuery<Person> query = new SelectQuery<>(connection, "SELECT * FROM person WHERE (age > ?)", Person.class);
QueryResult<Person> result = query.execute(18);

if (result.hasElements())
{
    // do something
}
else
{
    // do something
}
```

Insert:
```java
InsertQuery query = new InsertQuery(connection, "INSERT INTO person (first_name, last_name, age) VALUES (?, ?, ?)");
long id = query.execute("John", "Doe", 45);
```

Update:
```java
UpdateQuery query = new UpdateQuery(connection, "UPDATE person SET (age = ?) WHERE (id = ?)");
int rowsAffected = query.execute(46, 1); // age and id
```

Delete:
```java
DeleteQuery query = new DeleteQuery(connection, "DELETE FROM person WHERE (id = ?)");
int rowsAffected = deleteQuery.execute(playerId);
```

## Installation

Add the following code to your **pom.xml**:

```xml
<repositories>
    <repository>
        <id>jcenter</id>
        <url>https://jcenter.bintray.com</url>
    </repository>
</repositories>
```

and the dependency:

```xml
<dependency>
    <groupId>com.mauriciotogneri</groupId>
    <artifactId>inquiry</artifactId>
    <version>1.1.0</version>
</dependency>
```

or if you use Gradle:

```groovy
dependencies
{
    compile 'com.mauriciotogneri:inquiry:1.1.0'
}
```