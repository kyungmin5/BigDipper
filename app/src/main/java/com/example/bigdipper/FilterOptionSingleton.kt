package com.example.bigdipper

class FilterOptionSingleton private constructor(){
    val fieldFilterNames  = arrayListOf<FilterData>(
        FilterData("IT/컴퓨터","IT/Computer"),
        FilterData("경영/경제","Business/Economics"),
        FilterData("교육/공부","Education"),
        FilterData("기획/마케팅","PM/Marketing"),
        FilterData("요리/여행", "Cooking/Travel"),
        FilterData("종교/봉사","Religion/Volunteer"),
        FilterData("자연/환경", "Nature/Environment"),
        FilterData("정치/사회", "Politics/Society"),
        FilterData("문화/예술","Culture/Art"),
        FilterData("인문/과학", "Humanities/Science"),
        FilterData("자기계발/취미", "Self-Improvement/Hobby"),
        FilterData("고전문학", "Classics"),
        FilterData("주식/투자", "Investment"),
        FilterData("시/소설", "Poetry/Novel"))

    val ageGroupFilterNames  = arrayListOf<FilterData>(
        FilterData("성인","Adult"),
        FilterData("청소년","Adolescent"),
        FilterData("무관","All ages"))

    companion object {
                private var instance: FilterOptionSingleton? = null

                fun getInstance(): FilterOptionSingleton {
                    if (instance == null) {
                        instance = FilterOptionSingleton()
                    }
                    return instance!!
        }
    }
}