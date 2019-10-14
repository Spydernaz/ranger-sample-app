# Creating Atlas Entities #

## Create a new Entity TYPE ##

From the current directory, run:

```sh
curl -u admin:admin -H "Content-Type: application/json" -X POST -d @httproute.entitytype.json localhost:21000/api/atlas/v2/types/typedefs
curl -u admin:admin -H "Content-Type: application/json" -X POST -d @types.json localhost:21000/api/atlas/v2/types/typedefs
```

## Create a new Instance of the above Entity Type ##

From the current directory, run:

```sh
curl -u admin:admin -H "Content-Type: application/json" -X POST -d @httproute.entityinstance.json localhost:21000/api/atlas/v2/entity
curl -u admin:admin -H "Content-Type: application/json" -X POST -d @entities.start.json localhost:21000/api/atlas/v2/entity/bulk
curl -u admin:admin -H "Content-Type: application/json" -X POST -d @entities.new.json localhost:21000/api/atlas/v2/entity/bulk
```
