<script setup>
    import { ref, computed } from 'vue'
    import { CalendarView, CalendarViewHeader } from "vue-simple-calendar"

    const showDate = ref(new Date())
	
	const campaigns = ref([])
	const characters = ref([])
	const sessions = ref([])

	const items = ref([])
   
    loadData();

    function loadData(){
        //TODO loading icon
        fetch(`http://localhost:3300/api/campaigns`)
            .then(res1 => res1.json())
            .then(res1 => {
				campaigns.value = res1
				fetch(`http://localhost:3300/api/characters`)
					.then(res2 => res2.json())
					.then(res2 => {
						characters.value = res2
						fetch(`http://localhost:3300/api/sessions`)
							.then(res3 => res3.json())
							.then(res3 => {
								sessions.value = res3
								calculatePageData()
							})
					})
			})
    }
	
    function calculatePageData() {
		let master = {}
		campaigns.value.forEach((campaign)=>{
			master[campaign._id] = campaign.name
		})
		characters.value.forEach((character)=>{
			master[character._id] = master[character.parentId]+" - "+character.name
		})

		let tempItems = []
		sessions.value.forEach((session)=>{
			tempItems.push({
				id:session._id,
				startDate:session.date,
				classes:session.played.charAt(0).toLowerCase(),
				title:master[session.parentId]
			})
		})

		items.value = tempItems
	}
	
    function setShowDate(d) {
        showDate.value = d;
    }
</script>

<template>
    <div id="calendar-div">
        <!-- https://github.com/richardtallent/vue-simple-calendar -->
        <calendar-view class="theme-custom" :show-date="showDate" :items="items" :starting-day-of-week="1">
            <template #header="{ headerProps }">
				<calendar-view-header
					:header-props="headerProps"
					@input="setShowDate" />
			</template>
        </calendar-view>
    </div>
</template>

<style>

    .cv-wrapper{display:flex;flex-direction:column;flex-grow:1;height:100%;min-height:100%;max-height:100%;overflow-x:hidden;overflow-y:hidden}
	.cv-wrapper,.cv-wrapper div{box-sizing:border-box;line-height:1em;font-size:1em}
	.cv-header-days{display:flex;flex-grow:0;flex-shrink:0;flex-basis:auto;flex-flow:row nowrap;border-width:0 0 0 1px}
	.cv-header-day{display:flex;flex-grow:1;flex-shrink:0;flex-basis:0;flex-flow:row nowrap;align-items:center;justify-content:center;text-align:center;border-width:1px 1px 0 0}
	.cv-weeks{display:flex;flex-grow:1;flex-shrink:1;flex-basis:auto;flex-flow:column nowrap;border-width:0 0 1px 1px;overflow-y:auto;-ms-overflow-style:none;scrollbar-width:none}
	.cv-weeknumber{width:2rem;position:relative;text-align:center;border-width:1px 1px 0 0;border-style:solid;line-height:1}
	.cv-week{display:flex;flex-grow:1;flex-shrink:1;flex-basis:0;flex-flow:row nowrap;min-height:3em;border-width:0;position:relative;width:100%;overflow-y:auto;-ms-overflow-style:none}
	.cv-weekdays{display:flex;flex-grow:1;flex-shrink:0;flex-basis:0;flex-flow:row nowrap;direction:ltr;position:relative;overflow-y:auto;scrollbar-width:none}
	.cv-day{display:flex;flex-grow:1;flex-shrink:0;flex-basis:0;position:relative;position:sticky;top:0;border-width:1px 1px 0 0;direction:initial}
	.cv-day-number{height:auto;align-self:flex-start}
	.cv-day-number:hover:after{position:absolute;top:1rem;background-color:var(--cal-holiday-bg, #f7f7f7);border:var(--cal-holiday-border, 1px solid #f0f0f0);box-shadow:.1rem .1rem .2rem var(--cal-holiday-shadow, rgba(0, 0, 0, .25));padding:.2rem;margin:.5rem;line-height:1.2}
	.cv-day[draggable],.cv-item[draggable]{-webkit-user-select:none;-moz-user-select:none;user-select:none}
	.cv-item{position:absolute;white-space:nowrap;overflow:hidden;background-color:#f7f7f7;border-width:1px;direction:initial}
	.cv-wrapper.wrap-item-title-on-hover .cv-item:hover{white-space:normal;z-index:1}
	.cv-header-days,.cv-header-day,.cv-weeks,.cv-week,.cv-day,.cv-item{border-style:solid;border-color:#ddd}
	.cv-item .endTime:before{content:"-"}
	.cv-header-day,.cv-day-number,.cv-item{padding:.2em}
	.cv-day-number:before{margin-right:.5em}
	.cv-item.offset0{left:0}
	.cv-item.offset1{left:calc((100% / 7))}
	.cv-item.offset2{left:calc((200% / 7))}
	.cv-item.offset3{left:calc((300% / 7))}
	.cv-item.offset4{left:calc((400% / 7))}
	.cv-item.offset5{left:calc((500% / 7))}
	.cv-item.offset6{left:calc((600% / 7))}
	.cv-item.span1{width:calc((100% / 7) - .05em)}
	.cv-item.span2{width:calc((200% / 7) - .05em)}
	.cv-item.span3{width:calc((300% / 7) - .05em)}
	.cv-item.span4{width:calc((400% / 7) - .05em)}
	.cv-item.span5{width:calc((500% / 7) - .05em)}
	.cv-item.span6{width:calc((600% / 7) - .05em)}
	.cv-item.span7{width:calc(100% - .05em)}
	.cv-weeks::-webkit-scrollbar,.cv-weekdays::-webkit-scrollbar{width:0;background:transparent}
	.cv-header{display:flex;flex:0 1 auto;flex-flow:row nowrap;align-items:center;min-height:2.5em;border-width:1px 1px 0 1px}
	.cv-header .periodLabel{display:flex;flex:1 1 auto;flex-flow:row nowrap;min-height:1.5em;line-height:1;font-size:1.5em}
	.cv-header,.cv-header button{border-style:solid;border-color:#ddd}
	.cv-header-nav,.cv-header .periodLabel{margin:.1em .6em}
	.cv-header-nav button,.cv-header .periodLabel{padding:.4em .6em}
	.cv-header button{box-sizing:border-box;line-height:1em;font-size:1em;border-width:1px}

/* Header */

.theme-custom .cv-header-nav {
	display: flex;
	flex-direction: row;
	align-items: center;
}

.theme-custom .cv-header button:not(.currentPeriod) {
	font-family: Roboto, sans-serif;
	background-color: transparent;
	border: none;
	border-radius: 99em;
	font-size: 2em;
	color: #5f6368;
}

.theme-custom .cv-header button.currentPeriod {
	font-size: 1.2em;
	border-radius: 0.25em;
	background-color: rgba(0, 0, 0, 0);
}

.theme-custom .cv-header button:hover {
	background-color: rgb(100, 0, 167);
}


.theme-custom .cv-day.today .cv-day-number {
	background-color: rgb(0, 107, 48);
	color: #fff;
}

.theme-custom .cv-week {
	min-height: 4em;
}
/* Events */



.theme-custom .cv-item {
	border-color: transparent;
	border-radius: 0.25em;
	color: white;
}

.theme-custom .cv-item.r {
	background-color: rgb(0, 167, 56);
}

.theme-custom .cv-item.p {
	background-color: rgb(100, 0, 167);
}


.theme-custom .cv-item.isHovered {
	filter: brightness(50%);
}


</style>