import request from '@/utils/request'

// 上传图片
export function uploadImage(file, onUploadProgress) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/upload/image',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress
  })
}

// 上传视频
export function uploadVideo(file, onUploadProgress) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/upload/video',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress
  })
}

// 上传文件（默认上传图片）
export function uploadFile(file, onUploadProgress) {
  return uploadImage(file, onUploadProgress)
}

