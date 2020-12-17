## promise

### import
- maven

```
<dependency>
	<groupId>com.imyeego.promise</groupId>
	<artifactId>promise-all</artifactId>
	<version>1.0.5</version>
	<type>pom</type>
</dependency>
```
- gradle

`implementation 'com.imyeego.promise:promise-all:1.0.5'`
### usage

```
Promise.of(()->{
           Thread.sleep(4000);
           return "123";
        }).map(Integer::parseInt).then(s -> {
            Log.e("promise then", "" + s);
        }).ui(s -> {
            tv.setText(String.valueOf(s + 7));
            Toast.makeText(MainActivity.this, "" + (s + 7), Toast.LENGTH_SHORT).show();
//            throw new NullPointerException();
            Log.e("promise ui", Thread.currentThread().getName() + ": " + s);
            count = 9;
        }).then(s -> {
            Log.e("promise then", Thread.currentThread().getName() + ": " + count);
        }).excep(e -> {     // catch the exception
            e.printStackTrace();
        }).make();
```

## upload to maven
1. ./gradlew install
2. ./gradles bintrayUpload

