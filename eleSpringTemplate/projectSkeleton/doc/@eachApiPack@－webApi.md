@entity.desc@
======



## 1. 查询商品
#### 描述
*  商品的展示

#### 使用场景
*  待、已审核商品
*  待、已排期商品

#### WEB API 地址
```
POST
/getgoods
```

#### 权限
```
```

#### 输入参数
| 名称 | 类型  | 必填 | 传参位置 | 说明
|-----|------|------|--------|--------|
|activity_id|number|n|url|活动|
|zone_id|number|n|url|商圈|
|status|number|n|url|审核状态｛0：无，1:通过，2:拒绝｝|
|arrange_status|number|n|url|排期状态 0未排 1已排|
|arrange_week_date|number|n|url|排期日期 1-7 |

#### 返回参数
```
{"code":"200","msg":"success","data":[{"id":1,"activityId":3,"userId":1,"goodsId":1,"goodsName":"dd","originPrice":23.00,"total":100,"zoneId":0,"imgUrl":"33fefsdfsdfsd","isDeleted":0,"status":4,"arrangeStatus":1,"arrangeBeginDate":1440305111000,"arrangeEndDate":1440909911000,"goodsDescribe":"this is describe","zonePrice":12.00,"createdAt":1440400241000,"updatedAt":1440400241000},{"id":2,"activityId":3,"userId":1,"goodsId":2,"goodsName":"this is describe","originPrice":0.00,"total":0,"zoneId":0,"imgUrl":"ssssssssss","isDeleted":0,"status":4,"arrangeStatus":1,"arrangeBeginDate":1440305111000,"arrangeEndDate":1440909911000,"goodsDescribe":"this is describe","zonePrice":0.00,"createdAt":1440400253000,"updatedAt":1440400253000},{"id":3,"activityId":3,"userId":1,"goodsId":3,"goodsName":"xd","originPrice":0.00,"total":0,"zoneId":0,"imgUrl":"","isDeleted":0,"status":4,"arrangeStatus":1,"arrangeBeginDate":1440305111000,"arrangeEndDate":1440909911000,"goodsDescribe":"this iiiiiiis describe","zonePrice":0.00,"createdAt":1440400261000,"updatedAt":1440400261000}]}
```

## 2. 审核商品（批量）
#### 描述
* 审核商品，修改状态

#### 使用场景
* 

#### WEB API 地址
```
POST
/auditgoods

```

#### 权限
```
status、arrange_week_date、arrange_status只能传一个，List id 中，只能传同一个活动的id。
例如{"status":0,"id":[1,2,3]}
或 {"arrange_week_date":5,"id":[1,2,3]}
或 {"arrange_status":0,"id":[153,154]}

```

#### 输入参数
| 名称 | 类型  | 必填 | 传参位置 | 说明
|----|---------|------|-------|--------|
|status|number|n|json|审核状态｛0：无，1:通过，2:拒绝｝|
|arrange_week_date|number|n|json|排期 周几 1-7 |
|arrange_status|number|n|json|排期状态 0未排 1已排|
|id|number|y|json|商品id(list)|


#### 返回参数
```
code :200
msg : "success"
data : {
	success
}

```


## 3. 修改商品（单个）
#### 描述
* 修改排期商品

#### 使用场景
*  修改名称、描述、图片

#### WEB API 地址
```
post
/updategoods
```

#### 权限
```
```

#### 输入参数
| 名称 | 类型  | 必填 | 传参位置 | 说明
|-----|------|------|--------|--------|
|id|number|y|json|商品id|
|goods_name|varchar|n|json|商品名称|
|goods_describe|varchar|n|json|描述|
|img_url|varchar|n|json|图片|

#### 返回参数
```
code :"200"
msg : "success"
data : {
  SUCCESS
}
```

