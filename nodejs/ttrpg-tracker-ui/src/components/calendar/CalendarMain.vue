<script setup>
    import { ref, computed } from 'vue'
    import { CalendarView, CalendarViewHeader } from "vue-simple-calendar"

    const showDate = ref(new Date())
    
    function setShowDate(d) {
        this.showDate = d;
    }
</script>

<template>
    <div id="calendar-div">
        <!-- https://github.com/richardtallent/vue-simple-calendar -->
        <calendar-view class="theme-gcal" :show-date="showDate">
            <template #header="{ headerProps }">
				<calendar-view-header
					:header-props="headerProps"
					@input="setShowDate" />
			</template>
        </calendar-view>
    </div>
</template>

<style>
	#calendar-div {
		display: flex;
        flex-direction: column;
        flex-grow: 1;
	}

    .cv-wrapper{display:flex;flex-direction:column;flex-grow:1;height:100%;min-height:100%;max-height:100%;overflow-x:hidden;overflow-y:hidden}.cv-wrapper,.cv-wrapper div{box-sizing:border-box;line-height:1em;font-size:1em}.cv-header-days{display:flex;flex-grow:0;flex-shrink:0;flex-basis:auto;flex-flow:row nowrap;border-width:0 0 0 1px}.cv-header-day{display:flex;flex-grow:1;flex-shrink:0;flex-basis:0;flex-flow:row nowrap;align-items:center;justify-content:center;text-align:center;border-width:1px 1px 0 0}.cv-weeks{display:flex;flex-grow:1;flex-shrink:1;flex-basis:auto;flex-flow:column nowrap;border-width:0 0 1px 1px;overflow-y:auto;-ms-overflow-style:none;scrollbar-width:none}.cv-weeknumber{width:2rem;position:relative;text-align:center;border-width:1px 1px 0 0;border-style:solid;line-height:1}.cv-week{display:flex;flex-grow:1;flex-shrink:1;flex-basis:0;flex-flow:row nowrap;min-height:3em;border-width:0;position:relative;width:100%;overflow-y:auto;-ms-overflow-style:none}.cv-weekdays{display:flex;flex-grow:1;flex-shrink:0;flex-basis:0;flex-flow:row nowrap;direction:ltr;position:relative;overflow-y:auto;scrollbar-width:none}.cv-day{display:flex;flex-grow:1;flex-shrink:0;flex-basis:0;position:relative;position:sticky;top:0;border-width:1px 1px 0 0;direction:initial}.cv-day-number{height:auto;align-self:flex-start}.cv-day-number:hover:after{position:absolute;top:1rem;background-color:var(--cal-holiday-bg, #f7f7f7);border:var(--cal-holiday-border, 1px solid #f0f0f0);box-shadow:.1rem .1rem .2rem var(--cal-holiday-shadow, rgba(0, 0, 0, .25));padding:.2rem;margin:.5rem;line-height:1.2}.cv-day[draggable],.cv-item[draggable]{-webkit-user-select:none;-moz-user-select:none;user-select:none}.cv-item{position:absolute;white-space:nowrap;overflow:hidden;background-color:#f7f7f7;border-width:1px;direction:initial}.cv-wrapper.wrap-item-title-on-hover .cv-item:hover{white-space:normal;z-index:1}.cv-header-days,.cv-header-day,.cv-weeks,.cv-week,.cv-day,.cv-item{border-style:solid;border-color:#ddd}.cv-item .endTime:before{content:"-"}.cv-header-day,.cv-day-number,.cv-item{padding:.2em}.cv-day-number:before{margin-right:.5em}.cv-item.offset0{left:0}.cv-item.offset1{left:calc((100% / 7))}.cv-item.offset2{left:calc((200% / 7))}.cv-item.offset3{left:calc((300% / 7))}.cv-item.offset4{left:calc((400% / 7))}.cv-item.offset5{left:calc((500% / 7))}.cv-item.offset6{left:calc((600% / 7))}.cv-item.span1{width:calc((100% / 7) - .05em)}.cv-item.span2{width:calc((200% / 7) - .05em)}.cv-item.span3{width:calc((300% / 7) - .05em)}.cv-item.span4{width:calc((400% / 7) - .05em)}.cv-item.span5{width:calc((500% / 7) - .05em)}.cv-item.span6{width:calc((600% / 7) - .05em)}.cv-item.span7{width:calc(100% - .05em)}.cv-weeks::-webkit-scrollbar,.cv-weekdays::-webkit-scrollbar{width:0;background:transparent}.cv-header{display:flex;flex:0 1 auto;flex-flow:row nowrap;align-items:center;min-height:2.5em;border-width:1px 1px 0 1px}.cv-header .periodLabel{display:flex;flex:1 1 auto;flex-flow:row nowrap;min-height:1.5em;line-height:1;font-size:1.5em}.cv-header,.cv-header button{border-style:solid;border-color:#ddd}.cv-header-nav,.cv-header .periodLabel{margin:.1em .6em}.cv-header-nav button,.cv-header .periodLabel{padding:.4em .6em}.cv-header button{box-sizing:border-box;line-height:1em;font-size:1em;border-width:1px}
/*
**************************************************************
This theme is a loose approximation of Google Calendar's UI,
as of May 2020.
**************************************************************
*/

/* Header */

.theme-gcal {
	--event-color-tomato: #d50000;
	--event-color-flamingo: #e67c73;
	--event-color-tangerine: #f4511e;
	--event-color-banana: #f6bf26;
	--event-color-sage: #33b679;
	--event-color-basil: #0b8043;
	--event-color-peacock: #039be5;
	--event-color-blueberry: #3f51b5;
	--event-color-lavender: #7986cb;
	--event-color-grape: #8e24aa;
	--event-color-graphite: #616161;

	font-size: 12px;
	font-family: Roboto, sans-serif;
	font-weight: 400;
	border-color: rgb(32 33 36);
}

.theme-gcal .cv-header {
	font-weight: 400;
	padding: 0.6em;
}

.theme-gcal .periodLabel {
	font-size: 1.85em;
	font-weight: 400;
	color: #3c4043;
	margin-left: 0;
}

.theme-gcal .cv-header-nav {
	display: flex;
	flex-direction: row;
	align-items: center;
}

.theme-gcal .cv-header button:not(.currentPeriod) {
	font-family: Roboto, sans-serif;
	background-color: transparent;
	border: none;
	border-radius: 99em;
	font-size: 2em;
	color: #5f6368;
}

.theme-gcal .cv-header button:hover {
	background-color: rgba(32 33 36 3.9%);
}

.theme-gcal .cv-header button.currentPeriod {
	order: -1;
	padding: 0.6em 0.75em;
	margin-right: 1em;
	font-size: 1.2em;
	border-radius: 0.25em;
}

.theme-gcal .cv-header button.previousYear,
.theme-gcal .cv-header button.previousPeriod,
.theme-gcal .cv-header button.nextPeriod,
.theme-gcal .cv-header button.nextYear {
	font-family: "Material Icons", sans-serif;
	font-weight: 400;
	width: 1.5em;
	height: 1.5em;
	padding: 0;
	text-align: center;
	letter-spacing: -0.6em;
	padding-right: 0.6em;
}

.theme-gcal .cv-header button:disabled {
	color: #ccc;
}

.theme-gcal .cv-header-day {
	border-bottom: none;
	text-transform: uppercase;
	padding: 0.75em 0 0.25em;
	color: #70757a;
}

.theme-gcal .cv-week.week1 .cv-day {
	border-top: none;
}

/* Grid */

.theme-gcal .cv-weeknumber {
	color: #808080;
}

.theme-gcal .cv-weeknumber span {
	margin: 0;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.theme-gcal .cv-day-number {
	display: flex;
	border-radius: 99rem;
	height: 2em;
	width: auto;
	min-width: 2em;
	justify-content: center;
	margin: 0 auto;
	padding: 0.5em 0.4em;
	color: #3c4043;
}

.theme-gcal .cv-day.outsideOfMonth .cv-day-number {
	color: #70757a;
}

.theme-gcal .cv-day.today .cv-day-number {
	margin-top: 0.25rem;
	background-color: #1a73e8;
	color: #fff;
}

.theme-gcal .cv-day[aria-selected="true"] {
	background-color: #ffc;
}

/* Events */

.theme-gcal .cv-item {
	border-color: transparent;
	border-radius: 0.25em;
	background-color: var(--event-color-graphite);
	color: white;
	padding-top: 0.325em;
	padding-bottom: 0.325em;
	padding-left: 0.6em;
}

/* Override metrics to leave space at the end of each item */

.theme-gcal .cv-item.span1 {
	width: calc((100% / 7) - 0.75em);
}

.theme-gcal .cv-item.span2 {
	width: calc((200% / 7) - 0.75em);
}

.theme-gcal .cv-item.span3 {
	width: calc((300% / 7) - 0.75em);
}

.theme-gcal .cv-item.span4 {
	width: calc((400% / 7) - 0.75em);
}

.theme-gcal .cv-item.span5 {
	width: calc((500% / 7) - 0.75em);
}

.theme-gcal .cv-item.span6 {
	width: calc((600% / 7) - 0.75em);
}

.theme-gcal .cv-item.span7 {
	width: calc((700% / 7) - 0.75em);
}

.theme-gcal .cv-item.isHovered {
	filter: brightness(95%);
}

.theme-gcal .cv-item.continued {
	border-left-style: none;
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
}

/* Event Times */

.theme-gcal .cv-item .startTime,
.theme-gcal .cv-item .endTime {
	font-weight: bold;
	color: #666;
}

/* Drag and drop */

.theme-gcal .cv-day.draghover {
	box-shadow: inset 0 0 0.2em 0.2em yellow;
}


</style>