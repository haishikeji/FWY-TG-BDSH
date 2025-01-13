<template>
  <div style="width:100%;display:flex;flex-wrap:wrap;justify-content: space-between;padding:20px 0;border-bottom:4px solid #f1f1f1">
    <p style="width:100%;line-height:40px;">饿了么配送范围展示：</p>
    <div id="container" style="width:600px;height:550px;"></div>
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
  name: 'maps',
  props: ['mapDataele'],
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
      if (this.mapDataele && this.mapDataele !== null) {
        this.deliveryScopeScreenshot = this.mapDataele.coordinatePic
        centerMarker = new qq.maps.LatLng(this.mapDataele.lanlnt.split(',')[0], this.mapDataele.lanlnt.split(',')[1])
        zoomStart = 16
      } else {
        if (this.initOption !== null) {
          centerMarker = new qq.maps.LatLng(this.initOption.split(',')[0], this.initOption.split(',')[1])
        } else {
          centerMarker = new qq.maps.LatLng(30.57447, 103.92377)
        }
        zoomStart = 8
      }
      map = new qq.maps.Map(document.getElementById('container'), {
        center: centerMarker, // 暂时默认成都
        zoom: zoomStart
      })
      // 标注的生成与回显
      if (this.mapDataele && this.mapDataele !== null) {
        this.addMarker(new qq.maps.LatLng(this.mapDataele.lanlnt.split(',')[0], this.mapDataele.lanlnt.split(',')[1])) // 初始化回显标注
        //设置多边形路径以便回显
        let path3 = []
        for (const item of JSON.parse(this.mapDataele.coordinate)) {
          path3.push(new qq.maps.LatLng(parseFloat(item.lat), parseFloat(item.lng)))
        }
        this.showpolygon(path3)
      } else {
      }
    },
    /**
     * 标注
    */
    // 添加监听事件 获取鼠标单击事件（仅留一个标注点）
    markerOnly () {
      var that = this
      qq.maps.event.addListener(map, 'click', function(event) {
        that.addMarker(event.latLng)
        qq.maps.event.addListener(map, 'click', function(event) {
            that.deleteOverlays()   // 删除原有标注仅留下一个
            marker=new qq.maps.Marker({
                position:event.latLng,
                map:map
            });
        });
        const latlngs = event.latLng.getLat() + ',' + event.latLng.getLng() // 解析出来的点方便给后端
        sessionStorage.setItem('latlng', latlngs) // 储存经纬度
      })
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
    }
  }
}
</script>
