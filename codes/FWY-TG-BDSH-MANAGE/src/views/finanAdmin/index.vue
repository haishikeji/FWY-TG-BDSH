<!--
 * @Author: your name
 * @Date: 2021-08-26 16:49:54
 * @LastEditTime: 2021-08-28 20:04:09
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \hccd-web-admin\src\views\finanAdmin\index.vue
-->
<template>
    <div>
        <keep-alive>
            <router-view v-if="$route.meta.keepAlive"></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive"></router-view>
    </div>
</template>
