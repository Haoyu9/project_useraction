<template>
    <div id="charts">
        <div id="pvChart" :style="{width: '300px', height: '300px'}"></div>
        <div id="uvChart" :style="{width: '300px', height: '300px'}"></div>
    </div>
</template>

<script>
export default {
    name: "Charts",
    data () {
        return {

        }
    },
    mounted () {
        this.initPVChart ();
        this.initUVChart ();
    },
    methods: {
        initPVChart () {
            let pvChart = this.$echarts.init(document.getElementById('pvChart'));
            pvChart.setOption({
                title : {
                    text: 'PV统计',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                series : [
                    {
                        type: 'pie',
                        radius : '55%',
                        center: ['50%', '50%'],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            });
            //显示加载动画
            pvChart.showLoading();
            this.$http.get('./api/chart/pv').then((res) => {
                let data = JSON.parse(JSON.stringify(res.data));
                console.log(data);
                pvChart.hideLoading();
                pvChart.setOption({
                    series: [{
                        data: data
                    }]
                });
            })
        },
        initUVChart () {
            let uvChart = this.$echarts.init(document.getElementById('uvChart'));
            uvChart.setOption({
                title : {
                    text: 'UV统计',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                series : [
                    {
                        type: 'pie',
                        radius : '55%',
                        center: ['50%', '50%'],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            });
            //显示加载动画
            uvChart.showLoading();
            this.$http.get('./api/chart/uv').then((res) => {
                let data = JSON.parse(JSON.stringify(res.data));
                console.log(data);
                uvChart.hideLoading();
                uvChart.setOption({
                    series: [{
                        data: data
                    }]
                });
            })
        }
    }
}
</script>

<style scoped>

</style>