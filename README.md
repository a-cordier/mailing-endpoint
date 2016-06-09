# REST MAIL SERVICE ENDPOINT

## Requesting with node.js

```js
var fs = require('fs');
var rest = require('restler');

fs.stat("/tmp/image.png", function(err, stats) {
    rest.post("http://localhost:8080", {
    	encoding: "utf8",
        multipart: true,
        data: {
            "message": JSON.stringify({
  				"from": "pumsi@univ-lille2.fr",
  				"to": "antoine.cordier-2@univ-lille2.fr",
  				"subject": "sending mail with json",
  				"content": "hello world"
 
			}),
            "file": rest.file("/tmp/image.png", null, stats.size, null, "image/png")
        }
    }).on("complete", function(data) {
        console.log(data);
    });
});

```
