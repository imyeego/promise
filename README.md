## promise

---

### import
- maven

```
<dependency>
	<groupId>com.imyeego.promise</groupId>
	<artifactId>promise-all</artifactId>
	<version>1.0.0</version>
	<type>pom</type>
</dependency>
```
- gradle

`implementation 'com.imyeego.promise:promise-all:1.0.0'`
### usage

```
Promise.of(()->{
           Thread.sleep(4000);
           return "promise";
        }).then(s -> {
            // do something
        }).ui(s -> {
            // ui option
        }).make();
```
