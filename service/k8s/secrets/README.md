#### SETEC ASTRONOMY

You can create a secret from the kubectl command directly like this:

```shell
$ kubectl create secret generic mysecret --from-literal="password=mernk8s"
```

Of course, you can create secrets from a config file. See [example.yaml](example.yaml).

The values in the YAML file must be base64 encoded. **_Please be careful of newline characters._**

###### Option 1
```shell
# -n omits the trailing new line
$ echo -n "password" | base64
```

###### Option 2
```shell
cat /tmp/pass.txt | tr -d '\n' | base64
```

