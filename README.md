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

`implementation 'com.imyeego.promise:promise-all:1.0.2'`
### usage

```
Promise.of(()->{
           Thread.sleep(4000);
           return "123";
        }).map(Integer::parseInt).then(s -> {
            Log.e("promise", "" + s);
        }).ui(s -> {
            tv.setText(String.valueOf(s + 7));
            Toast.makeText(MainActivity.this, "" + (s + 7), Toast.LENGTH_SHORT).show();
        }).make();
```
