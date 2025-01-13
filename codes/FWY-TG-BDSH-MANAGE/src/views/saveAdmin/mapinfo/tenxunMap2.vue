<template>
  <div style="width:100%;display:flex;flex-wrap:wrap;justify-content: space-between;padding:20px 0;border-bottom:4px solid #f1f1f1">
    <p style="width:100%;line-height:40px;">美团配送范围展示：</p>
    <div id="container1" style="width:600px;height:550px;"></div>
  </div>
</template>
<script charset="utf-8" src="https://map.qq.com/api/js?v=2.exp&key=IFRBZ-NRYCJ-IQ6FH-F3UQF-B35SS-ULFVU&libraries=drawing"></script>
<script defer charset="utf-8" src="https://3gimg.qq.com/lightmap/components/geolocation/geolocation.min.js"></script>
<script>
import Serve from '@/serve/hc-api/save'
import { Message } from 'element-ui'
import { http } from '@/serve/aixos'
var map, geocoder, marker, polygon, drawingManager, lngLat, centerMarker, zoomStart
var markersArray=[], overlaysArray=[]
export default{
  name: 'mapsTwo',
  props: ['mapDatamt'],
  data () {
    return {
      keys: 'QTLBZ-YBJHW-I4ORS-R3247-VXY75-OXFUP',
      values: '', // 搜索内容
      deliveryScopeScreenshot: '', // 配送截图
      index: 0, // 判断重绘按钮
      values: '', // 搜索值
      pois: [], // 搜索结果
      searchLatlng: '', // 选择的搜索位置
      initOption: null // 初始化城市位置
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    init () {
      if (this.mapDatamt && this.mapDatamt !== null) {
        this.deliveryScopeScreenshot = this.mapDatamt.coordinatePic
        centerMarker = new qq.maps.LatLng(this.mapDatamt.lanlnt.split(',')[0], this.mapDatamt.lanlnt.split(',')[1])
        zoomStart = 16
      } else {
        if (this.initOption !== null) {
          centerMarker = new qq.maps.LatLng(this.initOption.split(',')[0], this.initOption.split(',')[1])
        } else {
          centerMarker = new qq.maps.LatLng(30.57447, 103.92377)
        }
        zoomStart = 8
      }
      map = new qq.maps.Map(document.getElementById('container1'), {
        center: centerMarker, // 暂时默认成都
        zoom: zoomStart
      })
      // 标注的生成与回显
      if (this.mapDatamt && this.mapDatamt !== null) {
        this.addMarker(new qq.maps.LatLng(this.mapDatamt.lanlnt.split(',')[0], this.mapDatamt.lanlnt.split(',')[1])) // 初始化回显标注
        //设置多边形路径以便回显
        let path3 = []
        for (const item of JSON.parse(this.mapDatamt.coordinate)) {
          path3.push(new qq.maps.LatLng(parseFloat(item.lat), parseFloat(item.lng)))
        }
        this.showpolygon(path3)
      } else {
      }
    },
    //添加标记
    addMarker(location) {
      this.deleteOverlays()
      marker = new qq.maps.Marker({
        position: location,
        map: map
      })
      markersArray.push(marker)
    },
    //删除标记
    deleteOverlays () {
      // 此注释代码在vue中会报错，在html里面不会。。原因未知
      // if (markersArray) {
      //   for (i in markersArray) {
      //     markersArray[i].setMap(null)
      //   }
      //   markersArray.length = 0
      // }
      markersArray.length = 0
      if (marker !== undefined) {
        marker.setMap(null)
      }
    },
    // 初始化回显
    showpolygon (path3) {
      polygon = new qq.maps.Polygon({
        map: map
      })
      polygon.setPath(path3)
    },
    // 删除初始化多边形
    delpolygon () {
      const a = []
      polygon.setPath(a)
      this.index = 1
      sessionStorage.removeItem('coordinates')
    },
    // 删除绘制的多边形
    clearOverlays () {
      if (overlaysArray.length !== 0) {
        overlaysArray[0].setMap(null)
      }
      overlaysArray = [] // 需要重置为空，否则之前的数据还在这个数组里面
      sessionStorage.removeItem('coordinates')
    }
  }
}
</script>
