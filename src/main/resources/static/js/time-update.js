const TIMESTAMP_UPDATE_INTERVAL = 10000; // 10초마다 글자 업데이트

// Moment 라이브러리의 전역 로케일 설정
moment.locale(document.documentElement.lang);

const updateTimeStrings = () => {
    const timeStamps = document.getElementsByClassName("timestamp");
    for (let stamp of timeStamps) {
        stamp.textContent = moment(stamp.dataset.timestamp).fromNow();
    }
};

setInterval(updateTimeStrings, TIMESTAMP_UPDATE_INTERVAL);