# REST MAIL SERVICE ENDPOINT

## Requesting with node.js

```js
var fs = require('fs');
var rest = require('restler');
var request = require('request');


/* Send message with attachment */
fs.stat("/tmp/image.png", function(err, stats) {
	rest.post("http://localhost:8080", {
		encoding: "utf8",
		multipart: true,
		data: {
			"message": JSON.stringify({ // note that json must be sent as a string in this case
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

/* Send a simple message without any attachment */
var options = {
  uri: "http://localhost:8080",
  method: 'POST',
  json: {
		"from": "pumsi@univ-lille2.fr",
		"to": "antoine.cordier-2@univ-lille2.fr",
		"subject": "sending mail with json",
		"content": "hello world"
	
  }
};
request.post(options, function(err, res, body) {
	console.log(res);
});
```
