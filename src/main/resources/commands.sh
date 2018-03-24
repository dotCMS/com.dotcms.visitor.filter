curl -XDELETE -H'Content-Type: application/json' http://localhost:9200/dotanalytics 


curl -XPUT -H'Content-Type: application/json' http://localhost:9200/dotanalytics -d'{

   "settings" : {
        "index" : {
            "number_of_shards" : 1, 
            "number_of_replicas" : 0
        }
    }
}
'




curl -XPUT -H'Content-Type: application/json' http://localhost:9200/dotanalytics/_mapping/metric -d'

{
    "properties": {
        "ip": {
            "type": "ip"
        },
        "ts": {
            "type": "date",
            "format": "epoch_millis"
        },
        "latLong": {
            "type": "geo_point"
        },
        "tags": {
            "type": "nested",
            "properties": {
                "tag": {
                    "type": "text"
                },
                "count": {
                    "type": "integer"
                }
            }
        }
    }
}
'