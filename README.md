
README
------

This bundle plugin provides a filter that logs information regarding a site visitor.  It requires that the user is running under the Tomcat app server.


How to build this example
-------------------------

To build this, run  `./gradlew jar`.  This will create two jars, both of which you should upload into your dotCMS.



```
{
	"response": 200,
	"clusterId": "1d010733-d391-4290-a4ce-c941295057dc",
	"sessionId": "5C6D6F3EF2F55D131DEEB5A49D2CE5A4",
	"ts": 1521938081967,
	"ip": "98.110.173.156",
	"vanityUrl": null,
	"request": "/contentAsset/image/335fc571-5d31-46e3-8f8a-d0c2b8fa7e15/banner/filter/Jpeg/jpeg_q/50/Resize/resize_w/1200/cloud%20computer.png",
	"query": {},
	"referer": "https://dotcms.com/",
	"host": "dotcms.com",
	"assetId": "unk",
	"contentId": "unk",
	"device": "COMPUTER",
	"agent": {
		"operatingSystem": "MAC_OS_X",
		"browser": "CHROME",
		"id": 50990849,
		"browserVersion": {
			"version": "65.0.3325.162",
			"majorVersion": "65",
			"minorVersion": "0"
		}
	},
	"userAgent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36",
	"cookies": {
		"__utmz": "1.1521937960.1.1.utmcsr",
		"__utmt": "1",
		"sitevisitscookie": "1",
		"__utmb": "1.2.10.1521937960",
		"opvc": "65590616-b903-4efa-8b43-48d12bbd0d12",
		"JSESSIONID": "5C6D6F3EF2F55D131DEEB5A49D2CE5A4",
		"__utmc": "1",
		"dmid": "9c7f36cd-52b0-4f12-b404-237c66834ec3",
		"__utma": "1.645710694.1521937960.1521937960.1521937960.1"
	},
	"persona": "unk",
	"city": "Andover",
	"country": "US",
	"lang": "en-us",
	"dmid": "9c7f36cd-52b0-4f12-b404-237c66834ec3",
	"latLong": "42.6489,-71.1655",
	"tags": [{"tag":"globalinvestor", "count":6}
		{"tag":"china", "count":4}
		{"tag":"europe", "count":3}
		{"tag":"housing", "count":2}
		{"tag":"asia", "count":2}
		{"tag":"retirement", "count":2}
		{"tag":"home prices", "count":2}
		{"tag":"pension", "count":2}
		{"tag":"retiree", "count":2}
		{"tag":"wealthyprospect", "count":2}
		{"tag":"estate planning", "count":1}
		{"tag":"research", "count":1}
		{"tag":"social security", "count":1}
		{"tag":"yield", "count":1}
		{"tag":"annuities", "count":1}
		{"tag":"medicare", "count":1}],
	"params": {},
	"pagesViewed": 4
}
```
