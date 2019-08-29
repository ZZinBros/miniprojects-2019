const TIMESTAMP_UPDATE_INTERVAL = 30 * 1000; // 30초마다 글자 업데이트

// Moment 라이브러리의 전역 로케일 설정
// TODO: document.documentElement.lang 사용 - 모든 페이지에 언어 설정 필요
moment.locale("ko");

const updateTimeStrings = () => {
    const timeStamps = document.getElementsByTagName("time");
    for (let timeStamp of timeStamps) {
        timeStamp.textContent = moment(timeStamp.dateTime).fromNow();
    }
};

updateTimeStrings();
setInterval(updateTimeStrings, TIMESTAMP_UPDATE_INTERVAL);