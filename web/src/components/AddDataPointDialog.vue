<template>
  <el-dialog :visible.sync="dialogVisible"
             :before-close="close"
             append-to-body
             custom-class="new-entry-dialog"
             v-loading="isLoading"
             :title="title"
             destroy-on-close>
    <div class="data-entry-form-wrapper">
      <el-form class="data-entry-form"
               :model="dataForm"
               :rules="dataFormRules"
               ref="dataEntryForm"
               inline
               label-width="120px">
        <el-form-item label="Data Type" prop="dataType">
          <el-radio-group v-model="dataForm.dataType" @change="handleDataTypeChange">
            <el-radio :label="0">Navaid</el-radio>
            <el-radio :label="1">Waypoint</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Data Format" prop="dataFormat">
          <el-radio-group v-model="dataForm.dataFormat">
            <el-radio :label="0" disabled>Standard (10m)</el-radio>
            <el-radio :label="1" disabled>Precision (1m)</el-radio>
            <el-radio :label="2">Raw Value</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item class="ident-input" label="Identification" prop="identification" :inline-message="true">
          <div class="ident-input-wrapper">
            <el-input class="ident-input-wrapper__input"
                      @input="handleIdentInput"
                      spellcheck="false"
                      maxlength="5"
                      v-model="dataForm.identification"></el-input>
            <el-collapse-transition>
              <div class="ident-input-wrapper__hint-outer" v-show="showIdentInputHint">
                <div>
                  <i class="el-icon-warning"></i>
                </div>
                <div class="ident-input-wrapper__hint">
                  Identification Code must contain 2 to 5 English alphabet letters or Arabic numbers
                </div>
              </div>
            </el-collapse-transition>

          </div>

        </el-form-item>
        <el-form-item label="Latitude" class="geo-coord-input-selector" prop="latitude">
          <div class="sphere-orientation-selector-wrapper" v-if="dataForm.dataFormat !== 2">
            <el-select v-model="dataForm.signLatitude">
              <el-option :value="1" label="N">N</el-option>
              <el-option :value="-1" label="S">S</el-option>
            </el-select>
          </div>
          <div class="sphere-position-input-wrapper">
            <el-input v-model="dataForm.latitude"
                      @input="handleLatitudeInput"
                      :placeholder="coordPlaceholder"></el-input>
          </div>
        </el-form-item>
        <el-form-item label="Longitude" class="geo-coord-input-selector" prop="longitude">
          <div class="sphere-orientation-selector-wrapper" v-if="dataForm.dataFormat !== 2">
            <el-select v-model="dataForm.signLongitude">
              <el-option :value="1" label="E">E</el-option>
              <el-option :value="-1" label="W">W</el-option>
            </el-select>
          </div>
          <div class="sphere-position-input-wrapper">
            <el-input v-model="dataForm.longitude"
                      @input="handleLongitudeInput"
                      :placeholder="coordPlaceholder"></el-input>
          </div>
        </el-form-item>
        <el-form-item label="Altitude" prop="altitude"
                      :rules="dataForm.dataType === 0 ? {required: true, message: 'Altitude is required for Navaid', trigger: 'blur'} : null">
          <div class="altitude-input-wrapper">
            <el-input v-model="dataForm.altitude">
              <div slot="append">
                <div class="altitude-unit-selector-wrapper">
                  <el-select v-model="unitOfMeasurement">
                    <el-option :value="0" label="FT">FT</el-option>
                    <el-option :value="1" label="M">M</el-option>
                  </el-select>
                </div>
              </div>
            </el-input>
          </div>
        </el-form-item>

        <el-form-item label="Description" class="nav-point-description" prop="description">
          <el-input v-model="dataForm.description" class="nav-point-description__input" type="textarea"></el-input>
        </el-form-item>

        <div class="button-wrapper">
          <el-button @click="submit">Submit</el-button>
          <el-button @click="close">Cancel</el-button>
        </div>
      </el-form>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "AddDataPointDialog",

    data() {
      const identValidator = (rule, value, callback) => {
        // should only contains english alphabet letter or arabic number
        if (/^[A-Z0-9]+$/.test(value)) {
          this.showIdentInputHint = false;
          return callback();
        } else {  // show hint
          this.showIdentInputHint = true;
          return callback(new Error())
        }
      };

      // const latitudeValidator = (rule, value, callback) => {
      //
      //   return callback();
      // }
      //
      // const longitudeValidator = (rule, value, callback) => {
      //
      //   return callback();
      // }

      return {
        isLoading: false,
        dialogVisible: false,

        showIdentInputHint: false,

        unitOfMeasurement: 0,

        latLastInput: '',
        lonLastInput: '',

        dataForm: {
          dataType: 0,
          dataFormat: 2,  // TODO: need to support other format
          identification: null,

          description: null,

          signLatitude: 1,
          signLongitude: 1,

          latitude: null,
          longitude: null,
          altitude: null
        },

        dataFormRules: {
          identification: [
            { required: true, validator: identValidator, trigger: 'blur'}
          ],

          latitude: [
              { required: true, message: 'Latitude is required', trigger: 'blur' }
          ],

          longitude: [
            { required: true, message: 'Longitude is required', trigger: 'blur' }
          ],

          // altitude: [
          //   { required: true, message: 'Altitude is required for Navaid', trigger: 'blur' }
          // ]

        }
      }
    },

    computed: {
      title() {
        return `Add ${this.dataForm.dataType === 0 ? 'Navigational Aid' : 'Waypoint'} Entry`;
      },

      coordPlaceholder() {
        switch (this.dataForm.dataFormat) {
          case 0:
            return 'dd mm ss.ss';
          case 1:
            return 'dd mm.mmm';
          case 2:
            return '(+/-)dd.dddddd, For example, -36.237568 or 42.342421';
          default:
            return '';
        }
      }
    },

    methods: {
      show() {
        this.dialogVisible = true;
      },

      close() {
        console.log(this.$refs.dataEntryForm);
        this.$refs.dataEntryForm.resetFields();

        this.showIdentInputHint = false;

        this.dialogVisible = false;
      },

      handleDataTypeChange() {
        this.$refs.dataEntryForm.clearValidate();
      },

      handleIdentInput(change) {
        this.dataForm.identification = change.toUpperCase();
      },

      handleLatitudeInput(change) {
        if (this.dataForm.dataFormat === 2) {
          if (/^(\+?|-?)\d*\.?\d*$/.test(change)) {
            this.latLastInput = change;  // preserve last valid input
          } else {
            this.dataForm.latitude = this.latLastInput;  // revert to last valid input
          }
        }
      },

      handleLongitudeInput(change) {
        if (this.dataForm.dataFormat === 2) {
          if (/^(\+?|-?)\d*\.?\d*$/.test(change)) {
            this.lonLastInput = change;  // preserve last valid input
          } else {
            this.dataForm.longitude = this.lonLastInput;  // revert to last valid input
          }
        }
      },

      submit() {
        this.$refs.dataEntryForm.validate(valid => {
          if (valid) {
            this.$message.success("ok");
          } else {
            this.$message.error("bad!");
          }
        });
      }
    }

  }
</script>

<style lang="scss" scoped>
  ::v-deep .el-dialog.new-entry-dialog {
    .el-dialog__body {

      .data-entry-form-wrapper {

        .data-entry-form {
          display: flex;
          flex-direction: column;

          .ident-input {
            display: flex;

            .el-form-item__content {
              flex: 1;
              margin-bottom: -12px;

              .ident-input-wrapper {

                display: flex;

                .ident-input-wrapper__input {
                  width: auto;

                  display: flex;
                  .el-input__inner {
                    font-size: 36px;
                    font-weight: bold;
                    margin-bottom: -12px;
                    font-family: monospace;
                    height: 42px;
                    width: 140px;
                  }
                }

                .ident-input-wrapper__hint-outer {
                  margin-left: 40px;
                  display: flex;

                  div {
                    color: #F56C6C;
                  }

                  .ident-input-wrapper__hint {
                    line-height: 22px;
                    word-break: break-word;
                    margin: auto 0 auto 10px;
                  }
                }
              }

              .el-form-item__error {
                display: none;
              }
            }
          }

          .geo-coord-input-selector {
            display: flex;

            .el-form-item__content {
              flex: 1;
              display: flex;
              .sphere-orientation-selector-wrapper {
                margin-right: 10px;
                width: 60px;
              }

              .sphere-position-input-wrapper {
                flex: 1;
              }
            }
          }

          .altitude-input-wrapper {

            .altitude-unit-selector-wrapper {
              width: 30px;
            }
          }

          .nav-point-description {
            display: flex;

            .el-form-item__content {
              flex: 1;
              .nav-point-description__input {
                min-height: 60px;
                max-height: 120px;

                textarea {
                  min-height: 60px !important;
                  max-height: 120px !important;
                }
              }
            }
          }

          .button-wrapper {
            display: flex;
            width: 100%;
            justify-content: flex-end;
          }
        }

      }
    }
  }
</style>