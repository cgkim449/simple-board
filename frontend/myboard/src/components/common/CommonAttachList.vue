<template>
    <v-card outlined class="px-1 pt-1 mt-3">
        <v-card-title class="text-subtitle-1 grey--text">
            첨부파일 {{ fetchedAttachList.length }}개
        </v-card-title>

        <v-card-text>
            <p v-for="attach in fetchedAttachList">
                <template v-if="attach.isImage">
                    <v-icon>mdi-attachment</v-icon>
                    {{ attach.name }}.{{ attach.extension }}
                    <a :href="attach.originalImageUri">
                        <v-img
                            :href="attach.originalImageUri"
                            v-bind:style="{cursor: 'pointer'}"
                            :src="attach.thumbnailUri"
                            class="white--text align-end"
                            gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                            width="200px"
                        >
                        </v-img>
                    </a>
                </template>

                <template v-else>
          <span v-on:click="downloadAttach(attach.attachId)" v-bind:style="{cursor: 'pointer'}">
            <v-icon>mdi-attachment</v-icon>
            {{ attach.name }}.{{ attach.extension }}
            <v-icon>mdi-download</v-icon>
          </span>
                </template>
            </p>
        </v-card-text>
    </v-card>
</template>

<script>
export default {
    name: "CommonAttachList",
    props: {
        fetchedAttachList: {
            type: Array,
        },

        attachOf: {
            type: String,
        },
    },
    methods: {
        downloadAttach(attachId) {
            if (this.attachOf === "board") {
                this.$_boardService.downloadAttach(attachId);

            } else if (this.attachOf === "question") {
                this.$_questionService.downloadAttach(attachId);

            } else if (this.attachOf === "answer") {
                this.$_answerService.downloadAttach(attachId);

            } else if (this.attachOf === "notice") {
                this.$_noticeService.downloadAttach(attachId);
            }
        },
    },
}
</script>

<style scoped>

</style>