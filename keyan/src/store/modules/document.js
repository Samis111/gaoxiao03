import axios from 'axios';

export default {
  namespaced: true,
  
  state: {
    uploadStatus: '',
    uploadProgress: 0,
  },

  mutations: {
    SET_UPLOAD_STATUS(state, status) {
      state.uploadStatus = status;
    },
    SET_UPLOAD_PROGRESS(state, progress) {
      state.uploadProgress = progress;
    }
  },

  actions: {
    async uploadDocument({ commit }, { file, description,submitter,submissionDate,id }) {
      try {
        commit('SET_UPLOAD_STATUS', 'uploading');
        
        const formData = new FormData();
        formData.append('file', file);
        formData.append('description', description);
        formData.append('submitter', submitter);
        formData.append('submissionDate', submissionDate);
        formData.append('id', id);

        const response = await axios.post('/api/file/Adminupload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          },
          onUploadProgress: (progressEvent) => {
            const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
            commit('SET_UPLOAD_PROGRESS', progress);
          }
        });

        if (response.data.code === 200) {
          commit('SET_UPLOAD_STATUS', 'success');
          return response.data;
        } else {
          throw new Error(response.data.message || '上传失败');
        }
      } catch (error) {
        commit('SET_UPLOAD_STATUS', 'error');
        throw error;
      }
    }
  }
}; 