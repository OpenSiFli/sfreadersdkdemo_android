

# SFReaderSDK 使用说明


## 1.1 概述

SFReaderSDK 提供从手表端读取 Log、Asset 等功能。

## 1.2 集成
### Gradle拉取配置
```
allprojects {
  repositories {
    maven {
      url 'https://maven.aliyun.com/repository/public'
    }
    maven {
      credentials {
        username '65a890ad4f41789f56425965'
        password 'Y-kxYEyTl5)['
      }
      url 'https://packages.aliyun.com/maven/repository/2457536-release-P0YgD8/'
    }
  }
}
```


```
   implementation 'com.sifli:siflicore:1.2.21'
   implementation 'com.sifli:siflireadersdk:1.0.9'
```

# 1.3 如何使用
SFReaderSDK 开放一个单例 SFReaderManager 作为接口对象。

### 初始化

```java
// 初始化
this.readerManager = SFReaderManager.getInstance();
this.readerManager.init(this.getApplication());

// 设置回调
this.readerManager.setManagerCallback(this);
```

## 1.3.1 实现回调
``` java
@Override
public void onManagerStatusChanged(int status) {
    Log.i(TAG, "onManagerStatusChanged = " + status);
}

@Override
public void onProgress(long currentBytes, long totalBytes) {
    int progress = 0;
    if (totalBytes > 0) {
        double percent = (double) currentBytes / totalBytes;
        progress = (int) (percent * 100);
    }
    int finalProgress = progress;
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            progressBar.setProgress(finalProgress);
        }
    });
}

@Override
public void onComplete(boolean success, SFReaderResult result, SFError error) {
    // 读取到的文件列表从 result.getRcvFiles() 返回
    String log = "task complete:" + success + ", rsp=" + result.getRequestCommand()
            + ", fileCount=" + result.getRcvFiles().size();
    Log.i(TAG, log);
    for (String path : result.getRcvFiles()) {
        Log.i(TAG, path);
    }
}
```
SFReaderSDK 通过回调通知 SDK 的状态、读取进度、任务执行结果。

### 响应结果对象
``` java
public class SFReaderResult {
    /**
     * SFReader 命令类型，对应 SFReaderCommandType
     */
    private int requestCommand;

    /**
     * SFReader 命令类型，对应 SFReaderCommandType
     */
    private int responseCommand;

    /**
     * 对于单一响应，该字段有效
     */
    private SFReaderResponsePacket responsePacket;

    /**
     * 文件读取类操作，该字段返回读取到的文件列表
     */
    private ArrayList<String> rcvFiles;
}
```
###  SDK 状态
```java
public class SFReaderManagerStatus {
    /** 空闲 */
    public final static int NONE = 0;
    /** 搜索并连接设备中 */
    public final static int SEARCH_AND_CONNECTING = 1;
    /** 模块工作中 */
    public final static int MODULE_WORKING = 2;
}
```

### 1.3.2 Asset
设置 Asset 开关

```java
this.readerManager.assetOptionRequest(option, targetMac);
```
其中 option 为 true/false，targetMac 为目标设备地址。

读取Asset
```java
this.readerManager.readAssetRequest(logDir, targetMac);
```
其中 logDir 为读取文件存放目录，targetMac 为目标设备地址。
读取成功后，文件地址通过回调通知，文件地址在 result.getRcvFiles() 列表之中。

### 1.3.3 AudioDump
### 读取AudioDump
```java
public void readAudioDumpRequest(String dir, String mac);
```
其中 dir 为读取文件存放目录，mac 为目标设备地址。

### 选择AudioDump类型
```java
public void openAudioDumpRequest(int type, String mac);
```
```bash
1	DOWLINK
2	DOWLINK_AGC
3	AUDPRC
4	DC_OUTPUT
5	RAMP_IN_OUTPUT
6	AECM_INPUT1
7	AECM_INPUT2
8	AECM_OUTPUT
9	ANC_OUTPUT
10	AGC_OUTPUT
11	RAMP_OUT_OUTPUT
```
### 清理 Dump

```
public void clearAudioDumpRequest(String mac);
```

## 1.4 错误码
```java
/** BLE 蓝牙异常 */
public final static int BLE_EXCEPTION = 1;

/** 蓝牙失去连接 */
public final static int BLE_DISCONNECTED = 2;

/** 连接失败 */
public final static int BLE_FAIL_TO_CONNECT = 3;

/** 监听通知失败 */
public final static int BLE_NOTIFY_LISTEN_FAIL = 4;

/** 设置 mtu 失败 */
public final static int BLE_SET_MTU_FAIL = 5;

/** 写入特征值失败 */
public final static int BLE_WRITE_CHARACTER_FAIL = 6;

/** 写入特征值失败，设备未处于握手状态 */
public final static int BLE_DEVICE_NOT_HANDSHAKE = 7;

/** 搜索设备超时 */
public final static int BLE_SEARCH_TIMEOUT = 8;

/** 蓝牙握手超时 */
public final static int BLE_HANDSHAKE_TIMEOUT = 9;

/** BLE 蓝牙不可用 */
public final static int BLE_NOT_ENABLE = 10;

/** BLE 发起连接时缺少 mac 地址 */
public final static int BLE_NO_MAC = 11;

/** 响应数据的包类型错误 */
public final static int SF_READER_INVALID_RSP_PACKET_TYPE = 110;

/** 响应数据长度和预期不匹配 */
public final static int SF_READER_RECEIVED_DATA_LEN_NOT_MATCH = 120;

/** 接收文件写入失败 */
public final static int SF_READER_WRITE_FILE_ERROR = 130;

/** 解析响应数据失败 */
public final static int SF_READER_FAIL_TO_PARSE_RSP_DATA = 140;

/** 响应数据 SERIAL_TRANSPORT 解析错误 */
public final static int SF_READER_SERIAL_TRANSPORT_RSP_ERROR = 150;

/** 任务超时 */
public final static int SF_READER_TASK_TIME_OUT = 160;

/** 人工停止 */
public final static int SF_READER_MANUAL_STOP = 170;

/** 获取 Audio dump 开始响应包失败 */
public final static int SF_READER_FAIL_TO_GET_AUDIO_DUMP_START_RSP = 180;
```


