<template>
    <v-container>
        <CommonPageTitle>
            <h2 slot="title" @click="moveToQuestionList" v-bind:style="{ cursor: 'pointer' }">
                Q&A
            </h2>
        </CommonPageTitle>

        <v-row justify="center">
            <v-col
                cols="auto"
            >

                <h3>질문</h3>
            </v-col>

            <v-spacer></v-spacer>

            <v-col
                cols="12"
            >
                <CommonItemDetail
                    v-bind:fetchedItemDetail="questionDetail"
                ></CommonItemDetail>

                <CommonAttachList
                    v-if="questionDetail.hasAttach"
                    v-bind:fetchedAttachList="questionDetail.attachList"
                    v-bind:attachOf="attachOf1"
                ></CommonAttachList>

            </v-col>
        </v-row>


        <v-row justify="center">
            <v-col
                cols="auto"
            >
                <h3>답변</h3>
            </v-col>

            <v-spacer></v-spacer>

            <template v-if="questionDetail.answer === null">
                <v-col
                    cols="12"
                >
                    <v-card outlined class="pa-1">
                        <v-card-text class="mt-1">
                            <v-row algin="center">
                                <v-col
                                    cols="auto"
                                >
                                    답변 처리중입니다.
                                </v-col>
                            </v-row>
                        </v-card-text>
                    </v-card>
                </v-col>
            </template>
            <!--      답변이 있으면 보여줌-->
            <template v-if="questionDetail.answer !== null">

                <v-col
                    cols="12"
                >
                    <CommonItemDetail
                        v-bind:fetchedItemDetail="questionDetail.answer"
                        v-bind:itemType="itemType"
                    ></CommonItemDetail>

                    <CommonAttachList
                        v-if="questionDetail.answer.hasAttach"
                        v-bind:fetchedAttachList="questionDetail.answer.attachList"
                        v-bind:attachOf="attachOf2"
                    ></CommonAttachList>
                </v-col>
            </template>

            <v-card elevation="0">
                <v-card-text>
                    <v-row justify="center">
                        <v-col
                            cols="auto"

                        >
                            <v-btn
                                @click="moveToQuestionList"
                                color="secondary"
                            >
                                목록
                            </v-btn>
                        </v-col>

                        <!-- 본인 글이고 답글 아직 안달려있으면 수정 삭제 버튼 보임. -->
                        <template
                            v-if="questionDetail.answer === null && $store.getters.loggedIn && ($store.state.username === questionDetail.memberUsername)">
                            <v-col
                                cols="auto"
                            >
                                <v-btn
                                    @click="moveToQuestionModify"
                                    outlined
                                    color="primary"
                                >
                                    수정
                                </v-btn>
                            </v-col>

                            <v-col
                                cols="auto"
                            >
                                <v-btn
                                    outlined
                                    color="primary"
                                    @click="removeQuestion()"
                                >
                                    삭제
                                </v-btn>
                            </v-col>

                        </template>
                    </v-row>
                </v-card-text>
            </v-card>
        </v-row>

        <v-row>
            <v-col>

            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import CommonItemDetail from "@/components/common/CommonItemDetail";
import CommonAttachList from "@/components/common/CommonAttachList";
import CommonPageTitle from "@/components/common/CommonPageTitle";

export default {
    name: "QuestionDetailView",
    components: {
        CommonAttachList,
        CommonItemDetail,
        CommonPageTitle
    },
    data() {
        return {
            questionDetail: {
                answer: {}
            },

            //TODO: 변수명 수정
            attachOf1: "question",
            attachOf2: "answer",
            itemType: "answer",
        }
    },
    computed: {},
    async created() {
        let questionId = this.$route.params.questionId;
        await this.fetchQuestionDetail(questionId);
    },
    methods: {
        async fetchQuestionDetail(questionId) {
            const {data} = await this.$_questionService.fetchQuestion(questionId);
            this.questionDetail = data.questionDetail;
        },

        async removeQuestion() {
            try {
                await this.$_questionService.removeQuestion(this.questionDetail.questionId);

                alert("삭제되었습니다.")

                this.moveToQuestionList();
            } catch (error) {
                console.log(error)
            }

        },
        moveToQuestionList() {
            this.$router.push({
                path: '/questions'
                , query: this.$route.query
            });
        },

        moveToQuestionModify() {
            this.$router.push({
                    name: "QuestionModifyView",
                    params: {
                        questionId: this.questionDetail.questionId
                    },
                    query: this.$route.query,
                }
            );
        }
    }
}
</script>

<style scoped>
.v-text-field >>> input {
    font-size: 0.875em;
}

.v-text-field >>> label {
    font-size: 0.875em;
}

.v-text-field >>> button {
    font-size: 0.875em;
}

.v-text-field--outlined >>> fieldset {
    border-color: rgba(209, 209, 209, 1);
}
</style>