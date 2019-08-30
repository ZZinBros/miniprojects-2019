const TIMESTAMP_UPDATE_INTERVAL = moment.duration({seconds: 30}); // 30초마다 글자 업데이트

// Moment 라이브러리의 전역 로케일 설정
// TODO: document.documentElement.lang 사용 - 모든 페이지에 언어 설정 필요
moment.locale("ko");

let serverTime;

const getServerTime = () => {
    fetch("/datetime")
        .then(response => response.json())
        .then(datetime => { serverTime = moment(datetime.datetime); })
        .catch(ignored => { serverTime = moment(); }); // fallback
};

const addIntervalToServerTime = () => {
    if (!serverTime) getServerTime();
    else serverTime = moment(serverTime).add(TIMESTAMP_UPDATE_INTERVAL);
};

const updateTimeStrings = () => {
    const timeStamps = document.getElementsByTagName("time");
    for (let timeStamp of timeStamps) {
        timeStamp.textContent = moment(timeStamp.dateTime).from(serverTime);
    }
};

const updateTimeStringsInterval = () => {
    addIntervalToServerTime();
    updateTimeStrings();
};

// 페이지 처음 로딩시 실행
getServerTime();
updateTimeStrings();
setInterval(updateTimeStringsInterval, TIMESTAMP_UPDATE_INTERVAL.asMilliseconds());