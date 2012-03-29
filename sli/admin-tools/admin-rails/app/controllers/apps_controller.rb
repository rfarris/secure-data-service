include ActiveSupport::Rescuable

class AppsController < ApplicationController

  rescue_from ActiveResource::ForbiddenAccess, :with => :render_403
  rescue_from ActiveResource::ResourceNotFound, :with => :render_404

  # GET /apps
  # GET /apps.json
  def index
    @title = "Application Registration Tool"
    @apps = App.all.sort { |a,b| b.metaData.updated <=> a.metaData.updated }
    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @apps }
    end
  end

  # GET /apps/1
  # GET /apps/1.json
  def show
    @app = App.find(params[:id])
  
    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @app }
    end
  end

  # GET /apps/new
  # GET /apps/new.json
  def new
    @title = "New Application"
    @app = App.new
    @app.developer_info = App::DeveloperInfo.new
  
    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @app }
    end
  end

  # # GET /apps/1/edit
  # def edit
  #   @app = App.find(params[:id])
  # end

  # POST /apps
  # POST /apps.json
  def create
    @app = App.new(params[:app])
    logger.debug{"Application is valid? #{@app.valid?}"}
    case @app.is_admin
    when "1"
      @app.is_admin = true
    when "0"
      @app.is_admin = false
    end

    case @app.enabled
    when "1"
      @app.enabled = true
    when "0"
      @app.enabled = false
    end
    
    case @app.developer_info.license_acceptance
    when "1"
      @app.developer_info.license_acceptance = true
    when "0"
      @app.developer_info.license_acceptance = false
    end
    
    respond_to do |format|
      if @app.save
        logger.debug {"Redirecting to #{apps_path}"}
        format.html { redirect_to apps_path, notice: 'App was successfully created.' }
        format.json { render json: @app, status: :created, location: @app }
        # format.js
      else
        format.html { render action: "new" }
        format.json { render json: @app.errors, status: :unprocessable_entity }
        # format.js
      end
    end
  end

  # PUT /apps/1
  # PUT /apps/1.json
  def update
    @app = App.find(params[:id])
    logger.debug {"App found (Update): #{@app.attributes}"}
    case params[:app][:is_admin]
    when "1"
      params[:app][:is_admin] = true
    when "0"
      params[:app][:is_admin] = false
    end

    case params[:app][:enabled]
    when "1"
      params[:app][:enabled] = true
    when "0"
      params[:app][:enabled] = false
    end
    
    case params[:app][:developer_info][:license_acceptance]
    when "1"
      params[:app][:developer_info][:license_acceptance] = true
    when "0"
      params[:app][:developer_info][:license_acceptance] = false
    end
    respond_to do |format|
      if @app.update_attributes(params[:app])
        format.html { redirect_to apps_path, notice: 'App was successfully updated.' }
        format.json { head :ok }
      else
        format.html { render action: "edit" }
        format.json { render json: @app.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /apps/1
  # DELETE /apps/1.json
  def destroy
    @app = App.find(params[:id])
    @app.destroy
  
    respond_to do |format|
      format.js
      # format.html { redirect_to apps_url }
      # format.json { head :ok }
    end
  end

end
